package com.nfinity.example179.domain.Flowable.Groups;

import com.nfinity.example179.domain.Flowable.Memberships.ActIdMembershipEntity;

import java.io.Serializable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "act_id_group", schema = "test8")
public class ActIdGroupEntity  implements Serializable {

	private String id;
	private String name;
	private Long rev;
	private String type;

	@OneToMany(mappedBy = "actIdGroup", cascade = CascadeType.ALL, orphanRemoval = true) 
	public Set<ActIdMembershipEntity> getActIdUser() {
		return actIdUser;
	}
	public void setActIdUser(Set<ActIdMembershipEntity> actIdUser) {
		this.actIdUser = actIdUser;
	}

	private Set<ActIdMembershipEntity> actIdUser = new HashSet<>(); 

    @Id
    @Column(name = "id_", nullable = false, length =64)
    public String getId() {
    	return id;
    }

    public void setId(String id){
    	this.id = id;
    }

    @Basic
    @Column(name = "name_", nullable = true, length =255)
    public String getName() {
    	return name;
    }

    public void setName(String name){
    	this.name = name;
    }

    @Basic
    @Column(name = "rev_", nullable = true)
    public Long getRev() {
    	return rev;
    }

    public void setRev(Long rev){
    	this.rev = rev;
    }

    @Basic
    @Column(name = "type_", nullable = true, length =255)
    public String getType() {
    	return type;
    }

    public void setType(String type){
    	this.type = type;
    }

    @Override
    public boolean equals(Object o) {
    	if (this == o) return true;
    	if (!(o instanceof ActIdGroupEntity)) return false;
    	ActIdGroupEntity actidgroup = (ActIdGroupEntity) o;
    	return id != null && id.equals(actidgroup.id);
    }
}





