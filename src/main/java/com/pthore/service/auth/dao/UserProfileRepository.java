package com.pthore.service.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pthore.service.auth.models.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long>{

	public UserProfile findByEmail(String email);

}
