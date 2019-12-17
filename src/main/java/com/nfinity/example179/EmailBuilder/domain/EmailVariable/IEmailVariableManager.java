package com.nfinity.example179.EmailBuilder.domain.EmailVariable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nfinity.example179.EmailBuilder.domain.model.EmailVariableEntity;

import com.querydsl.core.types.Predicate;

public interface IEmailVariableManager {

	 // CRUD Operations
    public EmailVariableEntity Create(EmailVariableEntity email);

    public void Delete(EmailVariableEntity email);

    public EmailVariableEntity Update(EmailVariableEntity email);

    public EmailVariableEntity FindById(Long emailId);
    
    public EmailVariableEntity FindByName (String name);

    public Page<EmailVariableEntity> FindAll(Predicate predicate,Pageable pageable);
	
	
}
