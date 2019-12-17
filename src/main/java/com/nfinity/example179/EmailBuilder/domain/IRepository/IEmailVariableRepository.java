package com.nfinity.example179.EmailBuilder.domain.IRepository;

import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nfinity.example179.EmailBuilder.domain.model.EmailVariableEntity;


@JaversSpringDataAuditable
@RepositoryRestResource(collectionResourceRel = "emailVariable", path = "emailVariable")
public interface IEmailVariableRepository extends JpaRepository<EmailVariableEntity, Long>, QuerydslPredicateExecutor<EmailVariableEntity> {

	   @Query("select e from EmailVariableEntity e where e.id = ?1")
	   EmailVariableEntity findById(long id);

	   @Query("select e from EmailVariableEntity e where e.propertyName = ?1")
	   EmailVariableEntity findByEmailName(String value);
	
}