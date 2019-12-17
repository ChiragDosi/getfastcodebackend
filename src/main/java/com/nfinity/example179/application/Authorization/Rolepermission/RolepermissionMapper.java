package com.nfinity.example179.application.Authorization.Rolepermission;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.nfinity.example179.domain.model.PermissionEntity;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.nfinity.example179.domain.model.RoleEntity;
import com.nfinity.example179.application.Authorization.Rolepermission.Dto.*;
import com.nfinity.example179.domain.model.RolepermissionEntity;


@Mapper(componentModel = "spring")
public interface RolepermissionMapper {

    RolepermissionEntity CreateRolepermissionInputToRolepermissionEntity(CreateRolepermissionInput rolepermissionDto);
   
   @Mappings({ 
   @Mapping(source = "permission.name", target = "permissionDescriptiveField"),                   
   @Mapping(source = "permission.id", target = "permissionId"),                   
   @Mapping(source = "role.name", target = "roleDescriptiveField"),                   
   @Mapping(source = "role.id", target = "roleId"),                   
   }) 
   CreateRolepermissionOutput RolepermissionEntityToCreateRolepermissionOutput(RolepermissionEntity entity);

    RolepermissionEntity UpdateRolepermissionInputToRolepermissionEntity(UpdateRolepermissionInput rolepermissionDto);

   @Mappings({ 
   @Mapping(source = "permission.name", target = "permissionDescriptiveField"),                   
   @Mapping(source = "permission.id", target = "permissionId"),                   
   @Mapping(source = "role.name", target = "roleDescriptiveField"),                   
   @Mapping(source = "role.id", target = "roleId"),                   
   }) 
   UpdateRolepermissionOutput RolepermissionEntityToUpdateRolepermissionOutput(RolepermissionEntity entity);

   @Mappings({ 
   @Mapping(source = "permission.name", target = "permissionDescriptiveField"),                   
   @Mapping(source = "permission.id", target = "permissionId"),                   
   @Mapping(source = "role.name", target = "roleDescriptiveField"),                   
   @Mapping(source = "role.id", target = "roleId"),                   
   }) 
   FindRolepermissionByIdOutput RolepermissionEntityToFindRolepermissionByIdOutput(RolepermissionEntity entity);


   @Mappings({
   @Mapping(source = "rolepermission.permissionId", target = "rolepermissionPermissionId"),
   @Mapping(source = "rolepermission.roleId", target = "rolepermissionRoleId"),
   })
   GetPermissionOutput PermissionEntityToGetPermissionOutput(PermissionEntity permission, RolepermissionEntity rolepermission);
 

   @Mappings({
   @Mapping(source = "rolepermission.permissionId", target = "rolepermissionPermissionId"),
   @Mapping(source = "rolepermission.roleId", target = "rolepermissionRoleId"),
   })
   GetRoleOutput RoleEntityToGetRoleOutput(RoleEntity role, RolepermissionEntity rolepermission);
 

}
