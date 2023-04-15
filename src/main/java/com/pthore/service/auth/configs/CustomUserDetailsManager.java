package com.pthore.service.auth.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

import com.pthore.service.auth.dao.UserAuthRepository;
import com.pthore.service.auth.models.UserAuth;

public class CustomUserDetailsManager implements UserDetailsManager {

	@Autowired
	private UserAuthRepository userAuthRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		return userAuthRepository.findByEmail(username);
	}

	@Override
	public void createUser(UserDetails user) {
		
		UserAuth customUser = (UserAuth) user;
		UserAuth result = userAuthRepository.save(customUser);
		if(result == null )
			throw new IllegalArgumentException("Wrong user object passed for registration");
	}

	@Override
	public void updateUser(UserDetails user) {
		
		UserAuth customUser = (UserAuth) user;
		UserAuth result = userAuthRepository.save(customUser);
		if(result == null )
			throw new IllegalArgumentException("Wrong user object passed for userUpdate");
		
	}

	@Override
	public void deleteUser(String username) {
		// TODO : add some validation 
		userAuthRepository.deleteByEmail(username);
		
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean userExists(String username) {
		return userAuthRepository.existsByEmail(username);
	}

}
