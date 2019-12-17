package com.nfinity.example179.domain.Authorization.Rolepermission;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.constraints.Positive;
import com.nfinity.example179.domain.model.RolepermissionEntity;
import com.nfinity.example179.domain.model.RolepermissionId;
import com.nfinity.example179.domain.model.PermissionEntity;
import com.nfinity.example179.domain.model.RoleEntity;

public interface IRolepermissionManager {
    // CRUD Operations
    RolepermissionEntity Create(RolepermissionEntity rolepermission);

    void Delete(RolepermissionEntity rolepermission);

    RolepermissionEntity Update(RolepermissionEntity rolepermission);

    RolepermissionEntity FindById(RolepermissionId rolepermissionId );

    Page<RolepermissionEntity> FindAll(Predicate predicate, Pageable pageable);
   
    //Permission
    public PermissionEntity GetPermission(RolepermissionId rolepermissionId );
  
    //Role
    public RoleEntity GetRole(RolepermissionId rolepermissionId );
  
}
