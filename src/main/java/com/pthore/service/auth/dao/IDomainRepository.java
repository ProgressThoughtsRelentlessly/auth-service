package com.pthore.service.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pthore.service.auth.models.Domain;

public interface IDomainRepository extends JpaRepository<Domain, String> {
	public Domain findByDomainName(String domainName);
}
