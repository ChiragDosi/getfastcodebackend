package com.nfinity.example179.application.Authorization.Role;

import com.nfinity.example179.application.Authorization.Role.Dto.*;
import com.nfinity.example179.domain.model.PermissionEntity;
import com.nfinity.example179.domain.model.RoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleEntity CreateRoleInputToRoleEntity(CreateRoleInput roleDto);

    CreateRoleOutput RoleEntityToCreateRoleOutput(RoleEntity entity);

    RoleEntity UpdateRoleInputToRoleEntity(UpdateRoleInput roleDto);

    UpdateRoleOutput RoleEntityToUpdateRoleOutput(RoleEntity entity);

    FindRoleByIdOutput RoleEntityToFindRoleByIdOutput(RoleEntity entity);
    
    FindRoleByNameOutput RoleEntityToFindRoleByNameOutput(RoleEntity entity);

}
