package com.nfinity.example179.domain.IRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;
import com.nfinity.example179.domain.model.Td1Entity;

@RepositoryRestResource(collectionResourceRel = "td1", path = "td1")
public interface ITd1Repository extends JpaRepository<Td1Entity, Long>,QuerydslPredicateExecutor<Td1Entity> {

}
