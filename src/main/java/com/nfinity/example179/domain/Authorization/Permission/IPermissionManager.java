package com.nfinity.example179.domain.Authorization.Permission;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.nfinity.example179.domain.model.PermissionEntity;

public interface IPermissionManager {
    // CRUD Operations
    PermissionEntity Create(PermissionEntity user);

    void Delete(PermissionEntity user);

    PermissionEntity Update(PermissionEntity user);

    PermissionEntity FindById(Long permissionId);

    Page<PermissionEntity> FindAll(Predicate predicate, Pageable pageable);

    //Internal operation
    PermissionEntity FindByPermissionName(String permissionName);
}
