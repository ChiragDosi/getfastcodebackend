package com.nfinity.example179.application.Flowable;

import com.nfinity.example179.domain.model.UserEntity;
import com.nfinity.example179.domain.Flowable.Users.ActIdUserEntity;
import org.springframework.stereotype.Component;

@Component
public class ActIdUserMapper {
	public ActIdUserEntity createUsersEntityToActIdUserEntity(UserEntity user) {
		if ( user == null ) {
			return null;
		}

		ActIdUserEntity actIdUser = new ActIdUserEntity();
		actIdUser.setId(user.getUserName());
		actIdUser.setFirst(user.getFirstName());
		actIdUser.setLast(user.getLastName());
		actIdUser.setEmail(user.getEmailAddress());
		actIdUser.setPwd(user.getPassword());
		actIdUser.setRev(0L);
		actIdUser.setDisplayName(null);
		actIdUser.setPictureId(null);
		actIdUser.setTenantId(null);
		
		return actIdUser;
	}

}
