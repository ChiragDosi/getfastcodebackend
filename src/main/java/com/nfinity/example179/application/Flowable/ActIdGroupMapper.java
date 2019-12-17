package com.nfinity.example179.application.Flowable;

import com.nfinity.example179.domain.model.RoleEntity;
import com.nfinity.example179.domain.Flowable.Groups.ActIdGroupEntity;
import org.springframework.stereotype.Component;

@Component
public class ActIdGroupMapper {
public ActIdGroupEntity createRolesEntityToActIdGroupEntity(RoleEntity role) {
    if ( role == null ) {
        return null;
    }

    ActIdGroupEntity actIdGroup = new ActIdGroupEntity();
    actIdGroup.setId(role.getName());
    actIdGroup.setName(role.getName());
    actIdGroup.setRev(0L);
    actIdGroup.setType(null);

    return actIdGroup;
    }
}
