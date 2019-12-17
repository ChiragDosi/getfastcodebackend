package com.nfinity.example179.domain.IRepository;

import com.nfinity.example179.domain.model.UserpermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.nfinity.example179.domain.model.UserpermissionEntity;

@RepositoryRestResource(collectionResourceRel = "userpermission", path = "userpermission")
public interface IUserpermissionRepository extends JpaRepository<UserpermissionEntity, UserpermissionId>,QuerydslPredicateExecutor<UserpermissionEntity> {
   
}
