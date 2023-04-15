package com.pthore.service.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pthore.service.auth.models.UserAuth;

public interface UserAuthRepository extends JpaRepository<UserAuth, Long>{
	
	public UserAuth findByEmail(String email);

	public boolean existsByEmail(String email);

	public void deleteByEmail(String email);
}
