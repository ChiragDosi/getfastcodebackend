package com.nfinity.example179.application.Authorization.Role;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nfinity.example179.CommonModule.Search.SearchCriteria;
import com.nfinity.example179.application.Authorization.Role.Dto.*;

import javax.validation.constraints.Positive;
import java.util.List;

@Service
public interface IRoleAppService {
    // CRUD Operations

    CreateRoleOutput Create(CreateRoleInput input);

    void Delete(Long rid);

    UpdateRoleOutput Update(Long roleId, UpdateRoleInput input);

    FindRoleByIdOutput FindById(Long rid);

    List<FindRoleByIdOutput> Find(SearchCriteria search, Pageable pageable) throws Exception;

}
