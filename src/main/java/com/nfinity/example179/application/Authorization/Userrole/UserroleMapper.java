package com.nfinity.example179.application.Authorization.Userrole;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.nfinity.example179.domain.model.UserEntity;
import com.nfinity.example179.domain.model.RoleEntity;
import com.nfinity.example179.application.Authorization.Userrole.Dto.*;
import com.nfinity.example179.domain.model.UserroleEntity;

@Mapper(componentModel = "spring")
public interface UserroleMapper {

   UserroleEntity CreateUserroleInputToUserroleEntity(CreateUserroleInput userroleDto);
   
   @Mappings({ 
   @Mapping(source = "user.userName", target = "userDescriptiveField"),                   
   @Mapping(source = "user.id", target = "userId"),  
   @Mapping(source = "role.name", target = "roleDescriptiveField"),                   
   @Mapping(source = "role.id", target = "roleId")                   
   }) 
   CreateUserroleOutput UserroleEntityToCreateUserroleOutput(UserroleEntity entity);

   UserroleEntity UpdateUserroleInputToUserroleEntity(UpdateUserroleInput userroleDto);

   @Mappings({ 
   @Mapping(source = "user.userName", target = "userDescriptiveField"),                   
   @Mapping(source = "user.id", target = "userId"),  
   @Mapping(source = "role.name", target = "roleDescriptiveField"),                   
   @Mapping(source = "role.id", target = "roleId")                  
   }) 
   UpdateUserroleOutput UserroleEntityToUpdateUserroleOutput(UserroleEntity entity);

   @Mappings({ 
   @Mapping(source = "user.userName", target = "userDescriptiveField"),                   
   @Mapping(source = "user.id", target = "userId"),  
   @Mapping(source = "role.name", target = "roleDescriptiveField"),                   
   @Mapping(source = "role.id", target = "roleId")                  
   })
   FindUserroleByIdOutput UserroleEntityToFindUserroleByIdOutput(UserroleEntity entity);

   @Mappings({
   @Mapping(source = "userrole.roleId", target = "userroleRoleId"),
   @Mapping(source = "userrole.userId", target = "userroleUserId")
   })
   GetUserOutput UserEntityToGetUserOutput(UserEntity user, UserroleEntity userrole);
 
   @Mappings({
   @Mapping(source = "userrole.userId", target = "userroleUserId"),
   @Mapping(source = "userrole.roleId", target = "userroleRoleId"),
   @Mapping(source = "role.id", target = "id")
   })
   GetRoleOutput RoleEntityToGetRoleOutput(RoleEntity role, UserroleEntity userrole);
 
}
