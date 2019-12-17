package com.nfinity.example179.Scheduler.domain;

import com.nfinity.example179.Scheduler.domain.IRepository.IJobHistoryRepository;
import com.nfinity.example179.Scheduler.domain.model.JobHistoryEntity;
import com.querydsl.core.types.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Repository
public class JobHistoryManager implements IJobHistoryManager {

    @Autowired
    private IJobHistoryRepository _jobRepository;


    @Transactional
    public JobHistoryEntity Create(JobHistoryEntity job) {

        return _jobRepository.save(job);
    }


    @Transactional
    public List<JobHistoryEntity> FindByJob(String jobName, String jobGroup) {
        return _jobRepository.findByJob(jobName, jobGroup);
    }

    @Transactional
    public List<JobHistoryEntity> FindByTrigger(String triggerName, String triggerGroup) {
        return _jobRepository.findByTrigger(triggerName, triggerGroup);
    }

    @Transactional
    public Page<JobHistoryEntity> FindAll(Predicate predicate,Pageable pageable) {
        return _jobRepository.findAll(predicate,pageable);
    }

    
//	 @Transactional
//	 public JobEntity GetJobById(Long userId)
//	    {
//	         return _jobRepository.findById(userId.longValue());
//	    }


//	@Override
//	public List<JobEntity> findAll(Sort sort) {
//		// TODO Auto-generated method stub
//		return null;
//	}


}

