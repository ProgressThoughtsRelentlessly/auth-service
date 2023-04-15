package com.pthore.service.auth.service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.AuthenticationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pthore.service.auth.dao.UserAuthRepository;
import com.pthore.service.auth.models.PasswordChangeRequest;
import com.pthore.service.auth.models.UserAuth;
import com.pthore.service.auth.utils.JwtTokenGenerator;
import com.pthore.service.auth.utils.JwtTokenValidator;


@Service
public class UserAuthorizationService {
	
	private Logger log = LoggerFactory.getLogger(UserAuthorizationService.class);
	
	@Autowired
	UserAuthRepository userAuthRepository;
	
	@Autowired 
	UserProfileService userProfileService;
	
	@Autowired
	private UserDetailsManager userDetailsManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public UserAuth upsertAuthUserAndUserProfile(OAuth2AuthenticationToken token) throws Exception {

		OAuth2User oAuth2User  = token.getPrincipal();
		Map<String, Object> attributes = oAuth2User.getAttributes();
		
		String username = oAuth2User.getAttribute("name");
		String email = oAuth2User.getAttribute("email");
		String picture = oAuth2User.getAttribute("picture");
		String role = ((GrantedAuthority) token.getAuthorities().toArray()[0]).getAuthority();
		
		UserAuth user = userAuthRepository.findByEmail(email);
		
		if(user == null) {
			user = new UserAuth();
			int lastNameIndex = username.indexOf(" ");
			user.setFirstname(username.substring(0, lastNameIndex));
			user.setLastname(username.substring(lastNameIndex).strip());
			
			List<GrantedAuthority> authorities= new ArrayList<>();
			authorities.add(new SimpleGrantedAuthority("READ"));
			authorities.add(new SimpleGrantedAuthority("WRITE"));
			user.setAuthorities(authorities);
			
			user.setEmail(email);
			user.setPictureUrl(picture);
			
			user = userAuthRepository.save(user);
			this.userProfileService.extractDataAndUpdateUserProfile(user);
		}
		return user;
	}
	
	
	public void persistJwtToken(UserAuth user, String jwtToken) {
		user.setJwtString(jwtToken);
		userAuthRepository.save(user);
	}
	
	public String generateJwt(UserAuth user) {

		Map<String, String> map = new HashMap<String, String>()
		{{
			put("email", user.getEmail());
			put("jwt-creation-time", LocalDate.now().toString());
		}};
		String jwtString = JwtTokenGenerator.encryptAndGenerateToken(map);
		return jwtString;
	}
	
	public String generateAndPersistJwtTokenForUser(UserAuth user) {
		
		String jwtString = generateJwt(user);
		persistJwtToken(user, jwtString);
		return jwtString;
	}
	
	public String handleLoginAndGenerateJwt(OAuth2AuthenticationToken token) throws Exception {
		
		UserAuth user = upsertAuthUserAndUserProfile(token);
		String jwtString = generateAndPersistJwtTokenForUser(user);
		
		log.info("jwt generated for user = {},\n"
				+ "JwtString = {}", user, jwtString);
		return jwtString;
	}
	
	public boolean isValidNewUser(UserAuth userAuth) {
		
		return !userAuthRepository.existsByEmail(userAuth.getEmail()) 
				&& 
				userAuth.getPassword() != userAuth.getMatchingPassword() ;
	}
	
	public void registerUser(UserAuth user) throws Exception{
		
		log.info("received a register request with the following details: {}", user);
		
		List<GrantedAuthority> authorities= new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("READ"));
		authorities.add(new SimpleGrantedAuthority("WRITE"));
		
		user.setAuthorities(authorities);
		
		if(!isValidNewUser(user)) {
		
			throw new AuthenticationException("Invalid registration details passed. Possibly user already exists or Passwords don't match.");
		}
		
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		user.setMatchingPassword(encodedPassword);
		
		userAuthRepository.save(user);
		userProfileService.extractDataAndUpdateUserProfile(user);
		
	}
	
	private boolean isValidJwt(String jwt) {
		Date expireDate = JwtTokenValidator.getJwtClaims(jwt).getExpiration();
		int isExpired = expireDate.compareTo(Date.from(Instant.now()));
		if(isExpired > 0)
			return true;
		return false;
	}

	// NOTE: BasicAuthenticationFilter will make sure the user Is loggedIn successfully. using the Bearer token.
	// Hence this implementation is not necessary. THIS WILL ONLY ADD REDUDANCY.
	@Deprecated
	public String loginUser(UserAuth user) throws Exception {

		String email = user.getEmail();
		String jwt = user.getJwtString();
		
		UserAuth userAuth = userAuthRepository.findByEmail(email);
		String jwtAuth = userAuth.getJwtString();
		
		if( jwt != null 
				&& jwtAuth != null 
				&& jwt.equals(jwtAuth) && isValidJwt(jwt)) {
			return jwt;
		}
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		if(!encodedPassword.equals(userAuth.getPassword())) { 
			
			throw new BadCredentialsException("wrong password");
		}
		// todo: Add this to JwtTokenGeneratorFilter.
		jwtAuth = generateJwt(userAuth);		
		userAuth.setJwtString(jwtAuth);
		return jwtAuth;
	}

	
	@Deprecated // LogoutFilter will handle this. THis step is redundant.
	public void logoutUser(Map<String, String> logoutObj) {
		UserAuth user = userAuthRepository.findByEmail(logoutObj.get("email"));
		user.setJwtString("");
		userAuthRepository.save(user);
	}

	// todo: Add this to JWTValidationFilter.
	public String verifyAndConfirmJwtString(String jwtString) {
		
		boolean isNonExpired = isValidJwt(jwtString);
		if(isNonExpired) {
			
			Map<String, String> jwtBody = JwtTokenValidator.getDecryptedJwtBodyAsMap(jwtString);
			if( !jwtBody.containsKey("email")) {
				return "invalid";
			}
			UserAuth userAuth = userAuthRepository.findByEmail( jwtBody.get("email") );
			
			if(userAuth.getJwtString().equals(jwtString)) {
				return "isValid";
			}
		}
		
		return "invalid";
	}


	public String getEmailFromOAuth2Token(OAuth2AuthenticationToken token) {
		
		OAuth2User oAuth2User  = token.getPrincipal();
		Map<String, Object> attributes = oAuth2User.getAttributes();
		String email = oAuth2User.getAttribute("email");
		return email;
	}
	
	private String encryptPassword(String password) {
		
		String encryptedPassword = this.passwordEncoder.encode(password);
		return encryptedPassword;
	}
	
	private boolean validateOldPasswordAndUpdateNewPassword(String email, String oldPassword, String newPassword) {
		
		if(oldPassword == null || newPassword == null || oldPassword.equals("") || newPassword.equals(""))
			return false;
		
		String encryptedOldPassword = encryptPassword(oldPassword);
		UserAuth userAuth = this.userAuthRepository.findByEmail(email);
		String encryptedOldPasswordFromDb = userAuth.getPassword();
		
		if(userAuth != null && encryptedOldPasswordFromDb.equals(encryptedOldPassword) ) {
		
			String encryptedNewPassword = encryptPassword(newPassword);
			userAuth.setPassword(encryptedNewPassword);
			this.userAuthRepository.saveAndFlush(userAuth);
			return true;
		}
		return false;
	}

	public boolean verifyOldPasswordAndUdpateToNewPassword(PasswordChangeRequest passwordChangeRequest) {
		
		String email = passwordChangeRequest.getEmail();
		String oldPass  = passwordChangeRequest.getOldPassword();
		String newPass = passwordChangeRequest.getNewPassword();
		return validateOldPasswordAndUpdateNewPassword(email, oldPass, newPass);
	}


	public boolean verifyEmailUsingJwt(String[] jwtAndEmail) {
		String jwt = jwtAndEmail[0];
		String email = jwtAndEmail[1];
		
		boolean isNonExpired = isValidJwt(jwt);
		if(isNonExpired) {
			
			Map<String, String> jwtBody = JwtTokenValidator.getDecryptedJwtBodyAsMap(jwt);
			if(!jwtBody.containsKey("email") || !jwtBody.get("email").equals(email)) {
				return false;
			}
			
			return true;
		}
		return false;
	}
	
	
}
