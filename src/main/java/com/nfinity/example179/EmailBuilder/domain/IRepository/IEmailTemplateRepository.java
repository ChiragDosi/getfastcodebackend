package com.nfinity.example179.EmailBuilder.domain.IRepository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nfinity.example179.EmailBuilder.domain.model.EmailTemplateEntity;

@JaversSpringDataAuditable
@RepositoryRestResource(collectionResourceRel = "email", path = "email")
public interface IEmailTemplateRepository extends JpaRepository<EmailTemplateEntity, Long>, QuerydslPredicateExecutor<EmailTemplateEntity> {

	   @Query("select e from EmailTemplateEntity e where e.id = ?1")
	   EmailTemplateEntity findById(long id);

	   @Query("select e from EmailTemplateEntity e where e.templateName = ?1")
	   EmailTemplateEntity findByEmailName(String value);
	
}
