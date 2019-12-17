package com.nfinity.example179.application.Authorization.Userpermission;

import java.util.List;

import javax.validation.constraints.Positive;
import com.nfinity.example179.domain.model.UserpermissionId;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.nfinity.example179.CommonModule.Search.SearchCriteria;
import com.nfinity.example179.application.Authorization.Userpermission.Dto.*;

@Service
public interface IUserpermissionAppService {

	CreateUserpermissionOutput Create(CreateUserpermissionInput userpermission);
    
    void Delete(UserpermissionId userpermissionId );

    UpdateUserpermissionOutput Update(UserpermissionId userpermissionId , UpdateUserpermissionInput input);

    FindUserpermissionByIdOutput FindById(UserpermissionId userpermissionId );

    List<FindUserpermissionByIdOutput> Find(SearchCriteria search, Pageable pageable) throws Exception;
	
	public UserpermissionId parseUserpermissionKey(String keysString);
    //user
    GetUserOutput GetUser(UserpermissionId userpermissionId );
    //Permission
    GetPermissionOutput GetPermission(UserpermissionId userpermissionId );
}
