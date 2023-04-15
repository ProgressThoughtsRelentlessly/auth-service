package com.pthore.service.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pthore.service.auth.dao.UserProfileRepository;
import com.pthore.service.auth.models.UserAuth;
import com.pthore.service.auth.models.UserProfile;
import com.pthore.service.auth.utils.UserUtils;

@Service
@Transactional
public class UserProfileService {
	
	@Autowired
	UserProfileRepository userProfileRepository;
	
	public void extractDataAndUpdateUserProfile(UserAuth user) throws Exception {
		
		UserProfile userProfile = UserUtils.getUserProfile(user);
		userProfileRepository.save(userProfile);
				
	}
	
	
}
