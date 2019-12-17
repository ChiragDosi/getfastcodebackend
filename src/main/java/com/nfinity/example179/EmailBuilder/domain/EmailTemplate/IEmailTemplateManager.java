package com.nfinity.example179.EmailBuilder.domain.EmailTemplate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nfinity.example179.EmailBuilder.domain.model.EmailTemplateEntity;

import com.querydsl.core.types.Predicate;

public interface IEmailTemplateManager {

	 // CRUD Operations
    public EmailTemplateEntity Create(EmailTemplateEntity email);

    public void Delete(EmailTemplateEntity email);

    public EmailTemplateEntity Update(EmailTemplateEntity email);

    public EmailTemplateEntity FindById(Long emailId);
    
    public EmailTemplateEntity FindByName (String name);

    public Page<EmailTemplateEntity> FindAll(Predicate predicate,Pageable pageable);
	
}
