package com.nfinity.example179.domain.Authorization.User;

import com.nfinity.example179.domain.model.UserEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserManager {
    // CRUD Operations
    UserEntity Create(UserEntity user);

    void Delete(UserEntity user);

    UserEntity Update(UserEntity user);

    UserEntity FindById(Long id);
    
    UserEntity FindByUserName(String userName);

    Page<UserEntity> FindAll(Predicate predicate, Pageable pageable);
  
}


