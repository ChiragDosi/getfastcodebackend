package com.nfinity.example179.domain.Authorization.Rolepermission;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import com.nfinity.example179.domain.model.RolepermissionEntity;
import com.nfinity.example179.domain.model.RolepermissionId;
import com.nfinity.example179.domain.IRepository.IPermissionRepository;
import com.nfinity.example179.domain.model.PermissionEntity;
import com.nfinity.example179.domain.IRepository.IRoleRepository;
import com.nfinity.example179.domain.model.RoleEntity;

import com.nfinity.example179.domain.IRepository.IRolepermissionRepository;
import com.querydsl.core.types.Predicate;

@Repository
public class RolepermissionManager implements IRolepermissionManager {

    @Autowired
    IRolepermissionRepository  _rolepermissionRepository;
    
    @Autowired
	IPermissionRepository  _permissionRepository;
    
    @Autowired
	IRoleRepository  _roleRepository;
    
	public RolepermissionEntity Create(RolepermissionEntity rolepermission) {

		return _rolepermissionRepository.save(rolepermission);
	}

	public void Delete(RolepermissionEntity rolepermission) {

		_rolepermissionRepository.delete(rolepermission);	
	}

	public RolepermissionEntity Update(RolepermissionEntity rolepermission) {

		return _rolepermissionRepository.save(rolepermission);
	}

	public RolepermissionEntity FindById(RolepermissionId rolepermissionId ) {
	
    Optional<RolepermissionEntity> dbRolepermission= _rolepermissionRepository.findById(rolepermissionId);
		if(dbRolepermission.isPresent()) {
			RolepermissionEntity existingRolepermission = dbRolepermission.get();
		    return existingRolepermission;
		} else {
		    return null;
		}

	}

	public Page<RolepermissionEntity> FindAll(Predicate predicate, Pageable pageable) {

		return _rolepermissionRepository.findAll(predicate,pageable);
	}

    //Permission
	public PermissionEntity GetPermission(RolepermissionId rolepermissionId) {
		
		Optional<RolepermissionEntity> dbRolepermission= _rolepermissionRepository.findById(rolepermissionId);
		if(dbRolepermission.isPresent()) {
			RolepermissionEntity existingRolepermission = dbRolepermission.get();
		    return existingRolepermission.getPermission();
		} else {
		    return null;
		}

	}
	
    //Role
	public RoleEntity GetRole(RolepermissionId rolepermissionId) {
		
		Optional<RolepermissionEntity> dbRolepermission= _rolepermissionRepository.findById(rolepermissionId);
		if(dbRolepermission.isPresent()) {
			RolepermissionEntity existingRolepermission = dbRolepermission.get();
		    return existingRolepermission.getRole();
		} else {
		    return null;
		}

	}
	
}
