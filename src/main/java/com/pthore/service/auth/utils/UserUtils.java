package com.pthore.service.auth.utils;

import com.pthore.service.auth.models.UserAuth;
import com.pthore.service.auth.models.UserProfile;

public class UserUtils {
	

	
	private static void mapToUserAuth(UserAuth userAuth, UserProfile userProfile) {
		userAuth.setEmail(userProfile.getEmail());
		userAuth.setFirstname(userProfile.getFirstName());
		userAuth.setLastname(userProfile.getLastname());
		userAuth.setJoiningDate(userProfile.getJoiningDate());
		userAuth.setMobile(userProfile.getMobile());
	}
	
	public static UserAuth getUserAuth(UserProfile userProfile) {
		UserAuth userAuth = new UserAuth();
		mapToUserAuth(userAuth, userProfile);
		return null;
	}
	
	
	private static void mapToUserProfile(UserAuth userAuth, UserProfile userProfile, boolean isRegistrationAction) {
		userProfile.setEmail(userAuth.getEmail());
		userProfile.setFirstName(userAuth.getFirstname());
		userProfile.setLastname(userAuth.getLastname());
		userProfile.setJoiningDate(userAuth.getJoiningDate());
		userProfile.setMobile(userAuth.getMobile());
		if(isRegistrationAction)
			userProfile.setLastUpdateDate(userAuth.getJoiningDate());
	}
	
	public static UserProfile getUserProfile(UserAuth userAuth) {
		
		UserProfile userProfile = new UserProfile();
		mapToUserProfile(userAuth, userProfile, true);
		return userProfile;
	}
}
