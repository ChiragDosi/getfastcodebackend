package com.nfinity.example179.application.Authorization.Userrole;

import java.util.List;
import javax.validation.constraints.Positive;
import com.nfinity.example179.domain.model.UserroleId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.nfinity.example179.CommonModule.Search.SearchCriteria;
import com.nfinity.example179.application.Authorization.Userrole.Dto.*;

@Service
public interface IUserroleAppService {

	CreateUserroleOutput Create(CreateUserroleInput userrole);

    void Delete(UserroleId userroleId);

    UpdateUserroleOutput Update(UserroleId userroleId, UpdateUserroleInput input);

    FindUserroleByIdOutput FindById(UserroleId userroleId);

    List<FindUserroleByIdOutput> Find(SearchCriteria search, Pageable pageable) throws Exception;

	public UserroleId parseUserroleKey(String keysString);
    
    //User
    GetUserOutput GetUser(UserroleId userroleId);
    
    //Role
    GetRoleOutput GetRole(UserroleId userroleId);
}
