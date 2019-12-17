package com.nfinity.example179.domain.Authorization.Userpermission;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import javax.validation.constraints.Positive;
import com.nfinity.example179.domain.model.UserpermissionEntity;
import com.nfinity.example179.domain.model.UserpermissionId;
import com.nfinity.example179.domain.model.UserEntity;
import com.nfinity.example179.domain.model.PermissionEntity;

public interface IUserpermissionManager {
    // CRUD Operations
    UserpermissionEntity Create(UserpermissionEntity userpermission);

    void Delete(UserpermissionEntity userpermission);

    UserpermissionEntity Update(UserpermissionEntity userpermission);

    UserpermissionEntity FindById(UserpermissionId userpermissionId );

    Page<UserpermissionEntity> FindAll(Predicate predicate, Pageable pageable);
   
    //User
    public UserEntity GetUser(UserpermissionId userpermissionId );
  
    //Permission
    public PermissionEntity GetPermission(UserpermissionId userpermissionId );
  
}
