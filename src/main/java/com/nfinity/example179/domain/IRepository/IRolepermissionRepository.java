package com.nfinity.example179.domain.IRepository;

import com.nfinity.example179.domain.model.RolepermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import com.nfinity.example179.domain.model.RolepermissionEntity;

@RepositoryRestResource(collectionResourceRel = "rolepermission", path = "rolepermission")
public interface IRolepermissionRepository extends JpaRepository<RolepermissionEntity, RolepermissionId>,QuerydslPredicateExecutor<RolepermissionEntity> {

	   
}
