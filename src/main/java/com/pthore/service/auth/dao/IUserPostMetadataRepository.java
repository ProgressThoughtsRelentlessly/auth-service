package com.pthore.service.auth.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pthore.service.auth.models.UserPostMetadata;
import com.pthore.service.auth.models.UserProfile;

public interface IUserPostMetadataRepository extends JpaRepository<UserPostMetadata, Long> {

	public List<UserPostMetadata> findByPostIdIn(List<String> postIds);

	public List<UserPostMetadata> findByUserProfile(UserProfile userProfile);

}
