package com.pthore.service.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pthore.service.auth.models.Follower;
import com.pthore.service.auth.models.UserProfile;

public interface IFollowersRepository extends JpaRepository<Follower, Long>{

	void deleteByEmailAndUser(String email, UserProfile targetUser);

	void deleteByEmail(String email);

}
