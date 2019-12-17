package com.nfinity.example179.domain.Flowable.Users;

public interface IActIdUserManager {

	public ActIdUserEntity findByUserId(String userId);
	
	public ActIdUserEntity findByUserEmail(String email);
	
	public void create(ActIdUserEntity actIdUser);
	
	public void delete(ActIdUserEntity actIdUser);
	
	public void update(ActIdUserEntity actIdUser);

	void deleteAll();
}
