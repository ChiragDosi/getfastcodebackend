package com.nfinity.example179.domain.Flowable.Memberships;

import com.nfinity.example179.domain.Flowable.Groups.ActIdGroupEntity;
import com.nfinity.example179.domain.Flowable.Users.ActIdUserEntity;

import java.io.Serializable;

import javax.persistence.*;

@Entity
@Table(name = "act_id_membership", schema = "test8")
@IdClass(MembershipId.class)
public class ActIdMembershipEntity {

	private String groupId;
	private String userId;

	@ManyToOne
	@JoinColumn(name = "group_id_", insertable=false, updatable=false)
	public ActIdGroupEntity getActIdGroup() {
  		return actIdGroup;
	}
	public void setActIdGroup(ActIdGroupEntity actIdGroup) {
  		this.actIdGroup = actIdGroup;
	}

	private ActIdGroupEntity actIdGroup;

	@ManyToOne
	@JoinColumn(name = "user_id_", insertable=false, updatable=false)
	public ActIdUserEntity getActIdUser() {
  		return actIdUser;
	}
	public void setActIdUser(ActIdUserEntity actIdUser) {
		this.actIdUser = actIdUser;
	}

	private ActIdUserEntity actIdUser;

	@Id
	@Column(name = "group_id_", nullable = false, length =64)
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId){
		this.groupId = groupId;
	}

	@Id
	@Column(name = "user_id_", nullable = false, length =64)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}
}

