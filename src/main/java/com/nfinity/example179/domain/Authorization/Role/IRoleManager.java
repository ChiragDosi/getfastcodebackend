package com.nfinity.example179.domain.Authorization.Role;

import com.nfinity.example179.domain.model.RoleEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nfinity.example179.CommonModule.Search.SearchFields;

import javax.validation.constraints.Positive;

public interface IRoleManager {

    // CRUD Operations
    RoleEntity Create(RoleEntity role);

    void Delete(RoleEntity role);

    RoleEntity Update(RoleEntity role);

    RoleEntity FindById(@Positive(message ="Id should be a positive value") Long roleId);

    //Internal operation
    RoleEntity FindByRoleName(String roleName);
    
    Page<RoleEntity> FindAll(Predicate predicate, Pageable pageable);

}

