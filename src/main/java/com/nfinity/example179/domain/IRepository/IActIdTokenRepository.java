package com.nfinity.example179.domain.IRepository;

import com.nfinity.example179.domain.Flowable.Tokens.ActIdTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "tokens", path = "tokens")
public interface IActIdTokenRepository extends JpaRepository<ActIdTokenEntity, String>, QuerydslPredicateExecutor<ActIdTokenEntity> {

    }
