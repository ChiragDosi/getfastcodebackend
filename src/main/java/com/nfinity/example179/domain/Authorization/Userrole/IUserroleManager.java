package com.nfinity.example179.domain.Authorization.Userrole;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.constraints.Positive;
import com.nfinity.example179.domain.model.UserroleEntity;
import com.nfinity.example179.domain.model.UserroleId;
import com.nfinity.example179.domain.model.UserEntity;
import com.nfinity.example179.domain.model.RoleEntity;

public interface IUserroleManager {
    // CRUD Operations
    UserroleEntity Create(UserroleEntity userrole);

    void Delete(UserroleEntity userrole);

    UserroleEntity Update(UserroleEntity userrole);

    UserroleEntity FindById(UserroleId userroleId);
	
    Page<UserroleEntity> FindAll(Predicate predicate, Pageable pageable);
   
    //User
    public UserEntity GetUser(UserroleId userroleId);
   
    //Role
    public RoleEntity GetRole(UserroleId userroleId);
}
