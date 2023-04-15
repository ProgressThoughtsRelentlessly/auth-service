package com.pthore.service.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pthore.service.auth.models.PthoreGroup;

public interface IGroupRepository extends JpaRepository<PthoreGroup, Long> {

}
