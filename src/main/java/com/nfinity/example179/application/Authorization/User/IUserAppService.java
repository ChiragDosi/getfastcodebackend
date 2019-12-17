package com.nfinity.example179.application.Authorization.User;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.nfinity.example179.CommonModule.Search.SearchCriteria;
import com.nfinity.example179.application.Authorization.User.Dto.*;

import java.util.List;

@Service
public interface IUserAppService {

	CreateUserOutput Create(CreateUserInput user);

    void Delete(Long id);

    UpdateUserOutput Update(Long userId, UpdateUserInput input);

    FindUserByIdOutput FindById(Long id);

    List<FindUserByIdOutput> Find(SearchCriteria search, Pageable pageable) throws Exception;

}
