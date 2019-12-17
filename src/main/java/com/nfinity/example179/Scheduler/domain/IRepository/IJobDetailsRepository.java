package com.nfinity.example179.Scheduler.domain.IRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nfinity.example179.Scheduler.domain.model.JobDetailsEntity;


@RepositoryRestResource(collectionResourceRel = "qrtz_job_details", path = "qrtz_job_details")

public interface IJobDetailsRepository extends JpaRepository<JobDetailsEntity, Long>, QuerydslPredicateExecutor<JobDetailsEntity>  {

   // @Query("select jobDetailsEntity from JobDetailsEntity jobDetailsEntity where jobDetailsEntity =predicate")
  //  Page<JobDetailsEntity> findAll(Predicate predicate,Pageable pageable);

}
