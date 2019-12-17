package com.nfinity.example179.EmailBuilder.domain.EmailTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nfinity.example179.EmailBuilder.domain.IRepository.IEmailTemplateRepository;
import com.nfinity.example179.EmailBuilder.domain.model.EmailTemplateEntity;
import com.querydsl.core.types.Predicate;

@Repository
public class EmailTemplateManager implements IEmailTemplateManager {

	@Autowired
	IEmailTemplateRepository  _emailTemplateRepository;

	public EmailTemplateEntity Create(EmailTemplateEntity email) {

		return _emailTemplateRepository.save(email);
	}

	public void Delete(EmailTemplateEntity email) {

		_emailTemplateRepository.delete(email);	
	}

	public EmailTemplateEntity Update(EmailTemplateEntity email) {

		return _emailTemplateRepository.save(email);
	}

	public EmailTemplateEntity FindById(Long emailId) {

		return _emailTemplateRepository.findById(emailId.longValue());
	}

	public Page<EmailTemplateEntity> FindAll(Predicate predicate, Pageable pageable) {

		return _emailTemplateRepository.findAll(predicate,pageable);
	}

	public EmailTemplateEntity FindByName(String name) {

		return _emailTemplateRepository.findByEmailName(name);
	}

}