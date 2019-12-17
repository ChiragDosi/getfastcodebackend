package com.nfinity.example179.EmailBuilder.domain.EmailVariable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nfinity.example179.EmailBuilder.domain.IRepository.IEmailVariableRepository;
import com.nfinity.example179.EmailBuilder.domain.model.EmailVariableEntity;
import com.querydsl.core.types.Predicate;

@Repository
public class EmailVariableManager implements IEmailVariableManager {

	@Autowired
	IEmailVariableRepository  _emailVariableRepository;

	public EmailVariableEntity Create(EmailVariableEntity email) {
		
		return _emailVariableRepository.save(email);
	}

	public void Delete(EmailVariableEntity email) {
		
		_emailVariableRepository.delete(email);	
	}

	public EmailVariableEntity Update(EmailVariableEntity email) {
		
		return _emailVariableRepository.save(email);
	}

	public EmailVariableEntity FindById(Long emailId) {
		
		return _emailVariableRepository.findById(emailId.longValue());
	}

	public EmailVariableEntity FindByName(String name) {
		
		return _emailVariableRepository.findByEmailName(name);
	}

	public Page<EmailVariableEntity> FindAll(Predicate predicate,Pageable pageable) {
		
		return _emailVariableRepository.findAll(predicate,pageable);
	}
	
	

}
