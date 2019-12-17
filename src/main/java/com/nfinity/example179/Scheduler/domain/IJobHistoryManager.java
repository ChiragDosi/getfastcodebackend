package com.nfinity.example179.Scheduler.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nfinity.example179.Scheduler.domain.model.JobHistoryEntity;

import com.querydsl.core.types.Predicate;


public interface IJobHistoryManager {

    // CRUD Operations
    JobHistoryEntity Create(JobHistoryEntity job);

    List<JobHistoryEntity> FindByJob(String jobName, String jobGroup);

    List<JobHistoryEntity> FindByTrigger(String triggerName, String triggerGroup);
    Page<JobHistoryEntity> FindAll(Predicate predicate,Pageable pageable);

}
