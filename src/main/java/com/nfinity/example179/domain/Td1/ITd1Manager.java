package com.nfinity.example179.domain.Td1;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.constraints.Positive;
import com.nfinity.example179.domain.model.Td1Entity;

public interface ITd1Manager {
    // CRUD Operations
    Td1Entity Create(Td1Entity td1);

    void Delete(Td1Entity td1);

    Td1Entity Update(Td1Entity td1);

    Td1Entity FindById(Long id);
	
    Page<Td1Entity> FindAll(Predicate predicate, Pageable pageable);
}
