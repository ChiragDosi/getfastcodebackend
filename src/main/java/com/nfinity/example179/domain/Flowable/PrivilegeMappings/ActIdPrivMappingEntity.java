package com.nfinity.example179.domain.Flowable.PrivilegeMappings;

import com.nfinity.example179.domain.Flowable.Privileges.ActIdPrivEntity;

import java.io.Serializable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;

@Entity
@Table(name = "act_id_priv_mapping", schema = "test8")
public class ActIdPrivMappingEntity  implements Serializable {

private String groupId;
private String id;
private String userId;

@ManyToOne
@JoinColumn(name = "priv_id_")
public ActIdPrivEntity getActIdPriv() {
return actIdPriv;
}
public void setActIdPriv(ActIdPrivEntity actIdPriv) {
this.actIdPriv = actIdPriv;
}

private ActIdPrivEntity actIdPriv;


@Basic
@Column(name = "group_id_", nullable = true, length =255)
public String getGroupId() {
return groupId;
}

public void setGroupId(String groupId){
this.groupId = groupId;
}


@Id
@Column(name = "id_", nullable = false, length =64)
public String getId() {
return id;
}

public void setId(String id){
this.id = id;
}


@Basic
@Column(name = "user_id_", nullable = true, length =255)
public String getUserId() {
return userId;
}

public void setUserId(String userId){
this.userId = userId;
}

@Override
public boolean equals(Object o) {
if (this == o) return true;
if (!(o instanceof ActIdPrivMappingEntity)) return false;
ActIdPrivMappingEntity actidprivmapping = (ActIdPrivMappingEntity) o;
return id != null && id.equals(actidprivmapping.id);
}
}





