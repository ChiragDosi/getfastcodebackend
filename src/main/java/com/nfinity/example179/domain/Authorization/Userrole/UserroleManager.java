package com.nfinity.example179.domain.Authorization.Userrole;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.nfinity.example179.domain.model.UserroleEntity;
import com.nfinity.example179.domain.model.UserroleId;
import com.nfinity.example179.domain.IRepository.IUserRepository;
import com.nfinity.example179.domain.model.UserEntity;
import com.nfinity.example179.domain.IRepository.IRoleRepository;
import com.nfinity.example179.domain.model.RoleEntity;
import com.nfinity.example179.domain.IRepository.IUserroleRepository;
import com.querydsl.core.types.Predicate;

@Repository
public class UserroleManager implements IUserroleManager {

    @Autowired
    IUserroleRepository  _userroleRepository;
    
    @Autowired
	IUserRepository  _userRepository;
    
    @Autowired
	IRoleRepository  _roleRepository;
    
	public UserroleEntity Create(UserroleEntity userrole) {

		return _userroleRepository.save(userrole);
	}

	public void Delete(UserroleEntity userrole) {

		_userroleRepository.delete(userrole);	
	}

	public UserroleEntity Update(UserroleEntity userrole) {

		return _userroleRepository.save(userrole);
	}

	public UserroleEntity FindById(UserroleId userroleId) {
    	Optional<UserroleEntity> dbUserrole= _userroleRepository.findById(userroleId);
		if(dbUserrole.isPresent()) {
			UserroleEntity existingUserrole = dbUserrole.get();
		    return existingUserrole;
		} else {
		    return null;
		}

	}

	public Page<UserroleEntity> FindAll(Predicate predicate, Pageable pageable) {

		return _userroleRepository.findAll(predicate,pageable);
	}
  
   //User
	public UserEntity GetUser(UserroleId userroleId) {
		
		Optional<UserroleEntity> dbUserrole= _userroleRepository.findById(userroleId);
		if(dbUserrole.isPresent()) {
			UserroleEntity existingUserrole = dbUserrole.get();
		    return existingUserrole.getUser();
		} else {
		    return null;
		}
	}
  
   //Role
	public RoleEntity GetRole(UserroleId userroleId) {
		
		Optional<UserroleEntity> dbUserrole= _userroleRepository.findById(userroleId);
		if(dbUserrole.isPresent()) {
			UserroleEntity existingUserrole = dbUserrole.get();
		    return existingUserrole.getRole();
		} else {
		    return null;
		}
	}
}
