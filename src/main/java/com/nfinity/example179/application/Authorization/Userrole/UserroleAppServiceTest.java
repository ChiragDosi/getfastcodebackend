package com.nfinity.example179.application.Authorization.Userrole;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nfinity.example179.domain.Authorization.Userrole.*;
import com.nfinity.example179.CommonModule.Search.*;
import com.nfinity.example179.application.Flowable.FlowableIdentityService;
import com.nfinity.example179.application.Authorization.Userrole.Dto.*;
import com.nfinity.example179.domain.model.QUserroleEntity;
import com.nfinity.example179.domain.model.UserroleEntity;
import com.nfinity.example179.domain.model.UserroleId;
import com.nfinity.example179.domain.model.UserEntity;
import com.nfinity.example179.domain.Authorization.User.UserManager;
import com.nfinity.example179.domain.model.RoleEntity;
import com.nfinity.example179.domain.Authorization.Role.RoleManager;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserroleAppServiceTest {

	@InjectMocks
	UserroleAppService _appService;

	@Mock
	private UserroleManager _userroleManager;
	
    @Mock
	private UserManager  _userManager;
	
    @Mock
	private RoleManager  _roleManager;
	
	@Mock
	private UserroleMapper _mapper;

	@Mock
	private Logger loggerMock;

	@Mock
	private LoggingHelper logHelper;
	
	@Mock
	private FlowableIdentityService idmIdentityService;

	@Mock
	private UserroleId userroleId;
	
	private static Long ID=15L;
	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(_appService);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void findUserroleById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.when(_userroleManager.FindById(any(UserroleId.class))).thenReturn(null);
		Assertions.assertThat(_appService.FindById(userroleId)).isEqualTo(null);
	}
	
	@Test
	public void findUserroleById_IdIsNotNullAndIdExists_ReturnUserrole() {

		UserroleEntity userrole = mock(UserroleEntity.class);
		Mockito.when(_userroleManager.FindById(any(UserroleId.class))).thenReturn(userrole);
		Assertions.assertThat(_appService.FindById(userroleId)).isEqualTo(_mapper.UserroleEntityToFindUserroleByIdOutput(userrole));
	}
	
	 @Test 
    public void createUserrole_UserroleIsNotNullAndUserroleDoesNotExist_StoreUserrole() { 
 
       UserroleEntity userroleEntity = mock(UserroleEntity.class); 
       CreateUserroleInput userrole = new CreateUserroleInput();
   
   
   
	//	UserEntity user= mock(UserEntity.class);
    //    userrole.setUserId(Long.valueOf(ID));
	//	Mockito.when(_userManager.FindById(
    //    any(Long.class))).thenReturn(user);
	//	RoleEntity role= mock(RoleEntity.class);
    //    userrole.setRoleId(Long.valueOf(ID));
	//	Mockito.when(_roleManager.FindById(
    //    any(Long.class))).thenReturn(role);
        
       Mockito.when(_mapper.CreateUserroleInputToUserroleEntity(any(CreateUserroleInput.class))).thenReturn(userroleEntity); 
       Mockito.when(_userroleManager.Create(any(UserroleEntity.class))).thenReturn(userroleEntity);
      
       Assertions.assertThat(_appService.Create(userrole)).isEqualTo(_mapper.UserroleEntityToCreateUserroleOutput(userroleEntity)); 
    } 
    @Test
	public void createUserrole_UserroleIsNotNullAndUserroleDoesNotExistAndChildIsNullAndChildIsNotMandatory_StoreUserrole() {

		UserroleEntity userroleEntity = mock(UserroleEntity.class);
		CreateUserroleInput userrole = mock(CreateUserroleInput.class);
		
		Mockito.when(_mapper.CreateUserroleInputToUserroleEntity(any(CreateUserroleInput.class))).thenReturn(userroleEntity);
		Mockito.when(_userroleManager.Create(any(UserroleEntity.class))).thenReturn(userroleEntity);
		Assertions.assertThat(_appService.Create(userrole)).isEqualTo(_mapper.UserroleEntityToCreateUserroleOutput(userroleEntity));

	}
	
	
    @Test
	public void updateUserrole_UserroleIsNotNullAndUserroleDoesNotExistAndChildIsNullAndChildIsNotMandatory_ReturnUpdatedUserrole() {

		UserroleEntity userroleEntity = mock(UserroleEntity.class);
		UpdateUserroleInput userrole = mock(UpdateUserroleInput.class);
		
		Mockito.when(_mapper.UpdateUserroleInputToUserroleEntity(any(UpdateUserroleInput.class))).thenReturn(userroleEntity);
		Mockito.when(_userroleManager.Update(any(UserroleEntity.class))).thenReturn(userroleEntity);
		Assertions.assertThat(_appService.Update(userroleId,userrole)).isEqualTo(_mapper.UserroleEntityToUpdateUserroleOutput(userroleEntity));
	}
	
		
	@Test
	public void updateUserrole_UserroleIdIsNotNullAndIdExists_ReturnUpdatedUserrole() {

		UserroleEntity userroleEntity = mock(UserroleEntity.class);
		UpdateUserroleInput userrole= mock(UpdateUserroleInput.class);
		Mockito.when(_mapper.UpdateUserroleInputToUserroleEntity(any(UpdateUserroleInput.class))).thenReturn(userroleEntity);
		Mockito.when(_userroleManager.Update(any(UserroleEntity.class))).thenReturn(userroleEntity);
		Assertions.assertThat(_appService.Update(userroleId,userrole)).isEqualTo(_mapper.UserroleEntityToUpdateUserroleOutput(userroleEntity));
	}
    
	@Test
	public void deleteUserrole_UserroleIsNotNullAndUserroleExists_UserroleRemoved() {

		UserroleEntity userrole= mock(UserroleEntity.class);
		Mockito.when(_userroleManager.FindById(any(UserroleId.class))).thenReturn(userrole);
		_appService.Delete(userroleId); 
		verify(_userroleManager).Delete(userrole);
	}
	
	@Test
	public void Find_ListIsEmpty_ReturnList() throws Exception {

		List<UserroleEntity> list = new ArrayList<>();
		Page<UserroleEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindUserroleByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");

		Mockito.when(_userroleManager.FindAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.Find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void Find_ListIsNotEmpty_ReturnList() throws Exception {

		List<UserroleEntity> list = new ArrayList<>();
		UserroleEntity userrole = mock(UserroleEntity.class);
		list.add(userrole);
    	Page<UserroleEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindUserroleByIdOutput> output = new ArrayList<>();
        SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");
		output.add(_mapper.UserroleEntityToFindUserroleByIdOutput(userrole));
    	Mockito.when(_userroleManager.FindAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.Find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void searchAllProperties_SearchIsNotNull_ReturnBooleanBuilder() {
		String search= "xyz";
		String operator= "equals";
		QUserroleEntity userrole = QUserroleEntity.userroleEntity;
		BooleanBuilder builder = new BooleanBuilder();
		Assertions.assertThat(_appService.searchAllProperties(userrole,search,operator)).isEqualTo(builder);
	}
	
	@Test
	public void searchSpecificProperty_PropertyExists_ReturnBooleanBuilder() {
		String operator= "equals";
		List<String> list = new ArrayList<>();
		
		QUserroleEntity userrole = QUserroleEntity.userroleEntity;
		BooleanBuilder builder = new BooleanBuilder();
		
		Assertions.assertThat(_appService.searchSpecificProperty(userrole, list,"xyz",operator)).isEqualTo(builder);
	}
	
	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QUserroleEntity userrole = QUserroleEntity.userroleEntity;
	    SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
	    Map<String,SearchFields> map = new HashMap<>();
		 Map<String,String> searchMap = new HashMap<>();
        searchMap.put("xyz",String.valueOf(ID));
		BooleanBuilder builder = new BooleanBuilder();
		Assertions.assertThat(_appService.searchKeyValuePair(userrole,map,searchMap)).isEqualTo(builder);
	}
	
	@Test (expected = Exception.class)
	public void checkProperties_PropertyDoesNotExist_ThrowException() throws Exception {
		List<String> list = new ArrayList<>();
		list.add("xyz");
		_appService.checkProperties(list);
	}
	
	@Test
	public void checkProperties_PropertyExists_ReturnNothing() throws Exception {
		List<String> list = new ArrayList<>();
		_appService.checkProperties(list);
	}
	
	@Test
	public void search_SearchIsNotNullAndSearchContainsCaseOne_ReturnBooleanBuilder() throws Exception {

		QUserroleEntity userrole = QUserroleEntity.userroleEntity;
		SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");
		BooleanBuilder builder = new BooleanBuilder();
		Assertions.assertThat(_appService.Search(search)).isEqualTo(builder);
	}
	
	@Test
	public void  search_SearchIsNotNullAndSearchContainsCaseTwo_ReturnBooleanBuilder() throws Exception {

		QUserroleEntity userrole = QUserroleEntity.userroleEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(2);
		search.setValue("xyz");
		search.setOperator("equals");
        fieldsList.add(fields);
        search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
		Assertions.assertThat(_appService.Search(search)).isEqualTo(builder);
	}
	
	@Test
	public void  search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
	
		Map<String,SearchFields> map = new HashMap<>();
		QUserroleEntity userrole = QUserroleEntity.userroleEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue("xyz");
		search.setOperator("equals");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
		Assertions.assertThat(_appService.Search(search)).isEqualTo(builder);
	}
	
	@Test
	public void  search_StringIsNull_ReturnNull() throws Exception {

		Assertions.assertThat(_appService.Search(null)).isEqualTo(null);
	}
	
   //User
	@Test
	public void GetUser_IfUserroleIdAndUserIdIsNotNullAndUserroleExists_ReturnUser() {
		UserroleEntity userrole = mock(UserroleEntity.class);
		UserEntity user = mock(UserEntity.class);

		Mockito.when(_userroleManager.FindById(any(UserroleId.class))).thenReturn(userrole);
		Mockito.when(_userroleManager.GetUser(any(UserroleId.class))).thenReturn(user);
		Assertions.assertThat(_appService.GetUser(userroleId)).isEqualTo(_mapper.UserEntityToGetUserOutput(user, userrole));
	}

	@Test 
	public void GetUser_IfUserroleIdAndUserIdIsNotNullAndUserroleDoesNotExist_ReturnNull() {
		UserroleEntity userrole = mock(UserroleEntity.class);

		Mockito.when(_userroleManager.FindById(any(UserroleId.class))).thenReturn(null);
		Assertions.assertThat(_appService.GetUser(userroleId)).isEqualTo(null);
	}
 
   //Role
	@Test
	public void GetRole_IfUserroleIdAndRoleIdIsNotNullAndUserroleExists_ReturnRole() {
		UserroleEntity userrole = mock(UserroleEntity.class);
		RoleEntity role = mock(RoleEntity.class);

		Mockito.when(_userroleManager.FindById(any(UserroleId.class))).thenReturn(userrole);
		Mockito.when(_userroleManager.GetRole(any(UserroleId.class))).thenReturn(role);
		Assertions.assertThat(_appService.GetRole(userroleId)).isEqualTo(_mapper.RoleEntityToGetRoleOutput(role, userrole));
	}

	@Test 
	public void GetRole_IfUserroleIdAndRoleIdIsNotNullAndUserroleDoesNotExist_ReturnNull() {
		UserroleEntity userrole = mock(UserroleEntity.class);

		Mockito.when(_userroleManager.FindById(any(UserroleId.class))).thenReturn(null);
		Assertions.assertThat(_appService.GetRole(userroleId)).isEqualTo(null);
	}
 

}

