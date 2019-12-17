package com.nfinity.example179.domain.Flowable.Privileges;

import java.io.Serializable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;

@Entity
@Table(name = "act_id_priv", schema = "test8")
public class ActIdPrivEntity  implements Serializable {

private String id;
private String name;


@Id
@Column(name = "id_", nullable = false, length =64)
public String getId() {
return id;
}

public void setId(String id){
this.id = id;
}


@Basic
@Column(name = "name_", nullable = false, length =255)
public String getName() {
return name;
}

public void setName(String name){
this.name = name;
}

@Override
public boolean equals(Object o) {
if (this == o) return true;
if (!(o instanceof ActIdPrivEntity)) return false;
ActIdPrivEntity actidpriv = (ActIdPrivEntity) o;
return id != null && id.equals(actidpriv.id);
}
}





