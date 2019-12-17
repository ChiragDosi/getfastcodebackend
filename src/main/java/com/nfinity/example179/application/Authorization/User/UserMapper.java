package com.nfinity.example179.application.Authorization.User;

import com.nfinity.example179.application.Authorization.User.Dto.*;
import com.nfinity.example179.domain.model.UserEntity;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /*
    CreateUserInput => User
    User => CreateUserOutput
    UpdateUserInput => User
    User => UpdateUserOutput
    User => FindUserByIdOutput
    Permission => GetPermissionOutput
    Role => GetRoleOutput
     */

    UserEntity CreateUserInputToUserEntity(CreateUserInput userDto);
   
    CreateUserOutput UserEntityToCreateUserOutput(UserEntity entity);

    UserEntity UpdateUserInputToUserEntity(UpdateUserInput userDto);

    UpdateUserOutput UserEntityToUpdateUserOutput(UserEntity entity);

    FindUserByIdOutput UserEntityToFindUserByIdOutput(UserEntity entity);
     
    FindUserByNameOutput UserEntityToFindUserByNameOutput(UserEntity entity);

    FindUserWithAllFieldsByIdOutput UserEntityToFindUserWithAllFieldsByIdOutput(UserEntity entity);
  
}