package com.pthore.service.auth.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pthore.service.auth.models.PasswordChangeRequest;
import com.pthore.service.auth.models.UserAuth;
import com.pthore.service.auth.service.UserAuthorizationService;

@RestController
@CrossOrigin(value= {"*"})
@RequestMapping(value = "/api", name = "LoginRegisterController")
public class LoginAndRegisterRestController {
	
	@Autowired
	UserAuthorizationService userAuthService;
	
	@PostMapping(value="/login")
	@Deprecated
	public ResponseEntity<?> loginUser(@RequestBody UserAuth loginObject)  throws Exception {
		
		String jwtString = userAuthService.loginUser(loginObject);
		HttpHeaders headers = new HttpHeaders();
		headers.add("jwt", jwtString);
		headers.add("email", loginObject.getEmail());
		
		return new ResponseEntity<>(headers, HttpStatus.OK);
	}
	
	@PostMapping(value="/register")
	public ResponseEntity<?> registerUser(@RequestBody UserAuth registrationObject ) throws Exception {
		
		userAuthService.registerUser(registrationObject);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/logout")
	@Deprecated
	public ResponseEntity<?> logoutUser(@RequestBody Map<String, String> logoutObj)  throws Exception {
		
		userAuthService.logoutUser(logoutObj);
		return new ResponseEntity<>(HttpStatus.OK);
	} 
	
	@PostMapping(value = "/auth/verify-jwt", name = "verify jwt produced by PSL-1")
	public ResponseEntity<?> verifyJwt(@RequestBody String jwtString )  throws Exception {
		
		String isValid = userAuthService.verifyAndConfirmJwtString(jwtString);
		return ResponseEntity.ok().body(isValid);
	} 
	
	@PostMapping(value = "/verify-user", name = "verify jwt produced by uds")
	public ResponseEntity<Boolean> verifyUserEmailUsingJwt(@RequestBody String[] jwtAndEmail )  throws Exception {
		
		boolean isValid = userAuthService.verifyEmailUsingJwt(jwtAndEmail);
		return ResponseEntity.ok().body(isValid);
	} 
	
	@PostMapping(value = "/updatePassword", name = "validate old password and update to new password")
	public ResponseEntity<?> verifyOldPasswordAndUpdateToNewPassword(@RequestBody PasswordChangeRequest passwordChangeRequest )  throws Exception {

		boolean isValidAndUpdatedToNewPassword = userAuthService.verifyOldPasswordAndUdpateToNewPassword(passwordChangeRequest);
		if(isValidAndUpdatedToNewPassword)
			return ResponseEntity.ok().body(isValidAndUpdatedToNewPassword);
		else 
			return ResponseEntity.badRequest().body("Enter the correct Old password and new Password should not be empty");
	} 
	
}
