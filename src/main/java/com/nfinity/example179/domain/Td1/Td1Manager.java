package com.nfinity.example179.domain.Td1;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.nfinity.example179.domain.model.Td1Entity;
import com.nfinity.example179.domain.IRepository.ITd1Repository;
import com.querydsl.core.types.Predicate;

@Repository
public class Td1Manager implements ITd1Manager {

    @Autowired
    ITd1Repository  _td1Repository;
    
	public Td1Entity Create(Td1Entity td1) {

		return _td1Repository.save(td1);
	}

	public void Delete(Td1Entity td1) {

		_td1Repository.delete(td1);	
	}

	public Td1Entity Update(Td1Entity td1) {

		return _td1Repository.save(td1);
	}

	public Td1Entity FindById(Long td1Id) {
    	Optional<Td1Entity> dbTd1= _td1Repository.findById(td1Id);
		if(dbTd1.isPresent()) {
			Td1Entity existingTd1 = dbTd1.get();
		    return existingTd1;
		} else {
		    return null;
		}

	}

	public Page<Td1Entity> FindAll(Predicate predicate, Pageable pageable) {

		return _td1Repository.findAll(predicate,pageable);
	}
}
