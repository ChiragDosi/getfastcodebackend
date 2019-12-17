package com.nfinity.example179.domain.Authorization.Userpermission;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.nfinity.example179.domain.model.UserpermissionEntity;
import com.nfinity.example179.domain.model.UserpermissionId;
import com.nfinity.example179.domain.IRepository.IUserRepository;
import com.nfinity.example179.domain.model.UserEntity;
import com.nfinity.example179.domain.IRepository.IPermissionRepository;
import com.nfinity.example179.domain.model.PermissionEntity;

import com.nfinity.example179.domain.IRepository.IUserpermissionRepository;
import com.querydsl.core.types.Predicate;

@Repository
public class UserpermissionManager implements IUserpermissionManager {

    @Autowired
    IUserpermissionRepository  _userpermissionRepository;
    
    @Autowired
	IUserRepository  _userRepository;
    
    @Autowired
	IPermissionRepository  _permissionRepository;
    
	public UserpermissionEntity Create(UserpermissionEntity userpermission) {

		return _userpermissionRepository.save(userpermission);
	}

	public void Delete(UserpermissionEntity userpermission) {

		_userpermissionRepository.delete(userpermission);	
	}

	public UserpermissionEntity Update(UserpermissionEntity userpermission) {

		return _userpermissionRepository.save(userpermission);
	}

	public UserpermissionEntity FindById(UserpermissionId userpermissionId ) {
    
    Optional<UserpermissionEntity> dbUserpermission= _userpermissionRepository.findById(userpermissionId);
		if(dbUserpermission.isPresent()) {
			UserpermissionEntity existingUserpermission = dbUserpermission.get();
		    return existingUserpermission;
		} else {
		    return null;
		}
    
	}

	public Page<UserpermissionEntity> FindAll(Predicate predicate, Pageable pageable) {

		return _userpermissionRepository.findAll(predicate,pageable);
	}

    //User
	public UserEntity GetUser(UserpermissionId userpermissionId) {
		
		Optional<UserpermissionEntity> dbUserpermission= _userpermissionRepository.findById(userpermissionId);
		if(dbUserpermission.isPresent()) {
			UserpermissionEntity existingUserpermission = dbUserpermission.get();
		    return existingUserpermission.getUser();
		} else {
		    return null;
		}

	}
	
   //Permission
	public PermissionEntity GetPermission(UserpermissionId userpermissionId) {
		
		Optional<UserpermissionEntity> dbUserpermission= _userpermissionRepository.findById(userpermissionId);
		if(dbUserpermission.isPresent()) {
			UserpermissionEntity existingUserpermission = dbUserpermission.get();
		    return existingUserpermission.getPermission();
		} else {
		    return null;
		}
	}
	
}
