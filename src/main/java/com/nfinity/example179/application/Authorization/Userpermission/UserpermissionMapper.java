package com.nfinity.example179.application.Authorization.Userpermission;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.nfinity.example179.domain.model.UserEntity;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.nfinity.example179.domain.model.PermissionEntity;
import com.nfinity.example179.application.Authorization.Userpermission.Dto.*;
import com.nfinity.example179.domain.model.UserpermissionEntity;


@Mapper(componentModel = "spring")
public interface UserpermissionMapper {

   UserpermissionEntity CreateUserpermissionInputToUserpermissionEntity(CreateUserpermissionInput userpermissionDto);
   
   @Mappings({ 
   @Mapping(source = "user.userName", target = "userDescriptiveField"),                   
   @Mapping(source = "user.id", target = "userId"),  
   @Mapping(source = "permission.name", target = "permissionDescriptiveField"),                   
   @Mapping(source = "permission.id", target = "permissionId")                   
   }) 
   CreateUserpermissionOutput UserpermissionEntityToCreateUserpermissionOutput(UserpermissionEntity entity);

   UserpermissionEntity UpdateUserpermissionInputToUserpermissionEntity(UpdateUserpermissionInput userpermissionDto);

   @Mappings({ 
   @Mapping(source = "user.userName", target = "userDescriptiveField"),                   
   @Mapping(source = "user.id", target = "userId"),  
   @Mapping(source = "permission.name", target = "permissionDescriptiveField"),                   
   @Mapping(source = "permission.id", target = "permissionId")                  
   }) 
   UpdateUserpermissionOutput UserpermissionEntityToUpdateUserpermissionOutput(UserpermissionEntity entity);

   @Mappings({ 
   @Mapping(source = "user.userName", target = "userDescriptiveField"),                   
   @Mapping(source = "user.id", target = "userId"),  
   @Mapping(source = "permission.name", target = "permissionDescriptiveField"),                   
   @Mapping(source = "permission.id", target = "permissionId")                  
   }) 
   FindUserpermissionByIdOutput UserpermissionEntityToFindUserpermissionByIdOutput(UserpermissionEntity entity);


   @Mappings({
   @Mapping(source = "userpermission.permissionId", target = "userpermissionPermissionId"),
   @Mapping(source = "userpermission.userId", target = "userpermissionUserId")
   })
   GetUserOutput UserEntityToGetUserOutput(UserEntity user, UserpermissionEntity userpermission);
 

   @Mappings({
   @Mapping(source = "userpermission.userId", target = "userpermissionUserId"),
   @Mapping(source = "userpermission.permissionId", target = "userpermissionPermissionId"),
   @Mapping(source = "permission.id", target = "id")
   })
   GetPermissionOutput PermissionEntityToGetPermissionOutput(PermissionEntity permission, UserpermissionEntity userpermission);
 

}
