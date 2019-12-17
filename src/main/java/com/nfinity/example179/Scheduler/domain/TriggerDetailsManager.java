package com.nfinity.example179.Scheduler.domain;

import com.nfinity.example179.Scheduler.domain.IRepository.ITriggerDetailsRepository;
import com.nfinity.example179.Scheduler.domain.model.TriggerDetailsEntity;
import com.querydsl.core.types.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public class TriggerDetailsManager {

	 @Autowired
	    private ITriggerDetailsRepository _triggerRepository;

	 @Transactional
	    public Page<TriggerDetailsEntity> FindAll(Predicate predicate,Pageable pageable) {
	        return _triggerRepository.findAll(predicate,pageable);
	    }


}