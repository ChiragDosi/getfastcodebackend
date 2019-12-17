package com.nfinity.example179.application.Authorization.Userpermission;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Spy;

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

import com.nfinity.example179.application.Flowable.FlowableIdentityService;
import com.nfinity.example179.domain.model.UserpermissionId;
import com.nfinity.example179.domain.Authorization.Userpermission.*;
import com.nfinity.example179.CommonModule.Search.*;
import com.nfinity.example179.application.Authorization.Userpermission.Dto.*;
import com.nfinity.example179.domain.model.QUserpermissionEntity;
import com.nfinity.example179.domain.model.UserpermissionEntity;
import com.nfinity.example179.domain.model.UserEntity;
import com.nfinity.example179.domain.Authorization.User.UserManager;
import com.nfinity.example179.domain.model.PermissionEntity;
import com.nfinity.example179.domain.Authorization.Permission.PermissionManager;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;


@RunWith(SpringJUnit4ClassRunner.class)
public class UserpermissionAppServiceTest {
    @Spy
	@InjectMocks
	UserpermissionAppService _appService;

	@Mock
	private UserpermissionManager _userpermissionManager;
	
    @Mock
	private UserManager  _userManager;
	
    @Mock
	private PermissionManager  _permissionManager;
	
	@Mock
	private UserpermissionMapper _mapper;

	@Mock
	private Logger loggerMock;

	@Mock
	private LoggingHelper logHelper;

	@Mock
	private FlowableIdentityService idmIdentityService;
	
	@Mock
	private UserpermissionId userPermissionId;
	
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
	public void findUserpermissionById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

    	Mockito.when(_userpermissionManager.FindById(any(UserpermissionId.class))).thenReturn(null);
		Assertions.assertThat(_appService.FindById(userPermissionId)).isEqualTo(null);
	}
	
	@Test
	public void findUserpermissionById_IdIsNotNullAndIdExists_ReturnUserpermission() {

		UserpermissionEntity userpermission = mock(UserpermissionEntity.class);
		Mockito.when(_userpermissionManager.FindById(any(UserpermissionId.class))).thenReturn(userpermission);
		Assertions.assertThat(_appService.FindById(userPermissionId)).isEqualTo(_mapper.UserpermissionEntityToFindUserpermissionByIdOutput(userpermission));
	}
	
	@Test 
    public void createUserpermission_UserpermissionIsNotNullAndUserIdOrPermssionIdIsNotNullAndUserpermissionDoesNotExist_StoreUserpermission() { 
 
        UserpermissionEntity userpermissionEntity = new UserpermissionEntity(); 
        CreateUserpermissionInput userpermission = mock(CreateUserpermissionInput.class); 
        UserEntity userEntity = mock (UserEntity.class);
		PermissionEntity permissionEntity=mock(PermissionEntity.class);
		Mockito.when(_userManager.FindById(any(Long.class))).thenReturn(userEntity);
		Mockito.when(_permissionManager.FindById(anyLong())).thenReturn(permissionEntity);
        Mockito.when(_mapper.CreateUserpermissionInputToUserpermissionEntity(any(CreateUserpermissionInput.class))).thenReturn(userpermissionEntity); 
        doNothing().when(idmIdentityService).addUserPrivilegeMapping(anyString(),anyString());
        Mockito.when(_userpermissionManager.Create(any(UserpermissionEntity.class))).thenReturn(userpermissionEntity); 
        
        Assertions.assertThat(_appService.Create(userpermission)).isEqualTo(_mapper.UserpermissionEntityToCreateUserpermissionOutput(userpermissionEntity)); 
    
    } 


    @Test 
	public void createUserpermission_UserpermissionInputIsNotNullAndUserIdOrPermssionIdIsNotNullAndPermissionIsAlreadyAssigned_StoreUserpermission() { 

		UserpermissionEntity userpermissionEntity = new UserpermissionEntity(); 
		CreateUserpermissionInput userpermission = mock(CreateUserpermissionInput.class);
		UserEntity userEntity = mock (UserEntity.class);
		PermissionEntity permissionEntity=mock(PermissionEntity.class);
		
		Mockito.doReturn(true).when(_appService).checkIfPermissionAlreadyAssigned(any(UserEntity.class), any(PermissionEntity.class));
		_appService.checkIfPermissionAlreadyAssigned(userEntity,permissionEntity);  
		verify(_appService).checkIfPermissionAlreadyAssigned(userEntity,permissionEntity);
		Mockito.when(_mapper.CreateUserpermissionInputToUserpermissionEntity(any(CreateUserpermissionInput.class))).thenReturn(userpermissionEntity); 
		Mockito.when(_userManager.FindById(any(Long.class))).thenReturn(userEntity);
		Mockito.when(_permissionManager.FindById(anyLong())).thenReturn(permissionEntity);
		Mockito.when(_userpermissionManager.Create(any(UserpermissionEntity.class))).thenReturn(userpermissionEntity); 
		doNothing().when(idmIdentityService).addUserPrivilegeMapping(anyString(),anyString());
        
		Assertions.assertThat(_appService.Create(userpermission)).isEqualTo(_mapper.UserpermissionEntityToCreateUserpermissionOutput(null)); 
	} 


	@Test
	public void createUserpermission_UserpermissionIsNotNullAndUserpermissionDoesNotExistAndUserIdOrPermssionIdIsNull_ReturnNull() {

		CreateUserpermissionInput userpermission = new CreateUserpermissionInput();
		userpermission.setPermissionId(null);

		Assertions.assertThat(_appService.Create(userpermission)).isEqualTo(null);
	}

	@Test
	public void createUserpermission_UserpermissionIsNotNullAndUserpermissionDoesNotExistAndUserIdOrPermssionIdIsNotNullAndUserOrPermissionDoesNotExist_ReturnNull() {

		CreateUserpermissionInput userpermission = mock(CreateUserpermissionInput.class);

		Mockito.when(_permissionManager.FindById(anyLong())).thenReturn(null);
		Assertions.assertThat(_appService.Create(userpermission)).isEqualTo(null);
	}	
	

    @Test
	public void updateUserpermission_UserpermissionIsNotNullAndUserpermissionExistAndUserIdOrPermssionIdIsNull_ReturnNull() {

		UpdateUserpermissionInput userpermission = new UpdateUserpermissionInput();
		userpermission.setPermissionId(null);

		Assertions.assertThat(_appService.Update(userPermissionId,userpermission)).isEqualTo(null);
	}

	@Test
	public void updateUserpermission_UserpermissionIsNotNullAndUserpermissionExistAndUserIdOrPermssionIdIsNotNullAndUserOrPermissionDoesNotExist_ReturnNull() {

		UpdateUserpermissionInput userpermission = mock(UpdateUserpermissionInput.class);

		Mockito.when(_permissionManager.FindById(anyLong())).thenReturn(null);
		Assertions.assertThat(_appService.Update(userPermissionId,userpermission)).isEqualTo(null);
	}


	@Test
	public void updateUserpermission_UserpermissionIsNotNullAndUserpermissionDoesNotExistAndChildIsNotNullAndChildIsNotMandatory_ReturnUpdatedUserpermission() {

		UserpermissionEntity userpermissionEntity = new UserpermissionEntity();
		UpdateUserpermissionInput userpermission = mock(UpdateUserpermissionInput.class);
		PermissionEntity permissionEntity= mock(PermissionEntity.class);
		UserEntity userEntity = mock (UserEntity.class);
		userpermissionEntity.setPermission(permissionEntity);

		Mockito.when(_mapper.UpdateUserpermissionInputToUserpermissionEntity(any(UpdateUserpermissionInput.class))).thenReturn(userpermissionEntity);
		Mockito.when(_userManager.FindById(any(Long.class))).thenReturn(userEntity);
		Mockito.when(_permissionManager.FindById(anyLong())).thenReturn(permissionEntity); 
		doNothing().when(idmIdentityService).updateUserPrivilegeMapping(anyString(),anyString());
		Mockito.when(_userpermissionManager.Update(any(UserpermissionEntity.class))).thenReturn(userpermissionEntity);

		Assertions.assertThat(_appService.Update(userPermissionId,userpermission)).isEqualTo(_mapper.UserpermissionEntityToUpdateUserpermissionOutput(userpermissionEntity));

	}
    
    @Test 
	public void checkIfPermissionAlreadyAssigned_UserEntityAndPermissionEntityIsNotNullAndUserPermissionSetIsEmpty_ReturnFalse() {

	  //UserpermissionEntity userpermission= mock(UserpermissionEntity.class);
		PermissionEntity permissionEntity= mock(PermissionEntity.class);
		UserEntity userEntity = mock (UserEntity.class);
	  //Mockito.when(userEntity.getRole()).thenReturn(null);
		Assertions.assertThat(_appService.checkIfPermissionAlreadyAssigned(userEntity, permissionEntity)).isEqualTo(false);

	}
	
	@Test
	public void deleteUserpermission_UserpermissionIsNotNullAndUserpermissionExists_UserpermissionRemoved() {

		UserpermissionEntity userpermission= mock(UserpermissionEntity.class);
		 Mockito.when(_userpermissionManager.FindById(any(UserpermissionId.class))).thenReturn(userpermission);
		
        PermissionEntity permissionEntity= mock(PermissionEntity.class);
		UserEntity userEntity = mock (UserEntity.class);
       
		Mockito.when(_userManager.FindById(any(Long.class))).thenReturn(userEntity);
		Mockito.when(_permissionManager.FindById(anyLong())).thenReturn(permissionEntity); 
		doNothing().when(idmIdentityService).updateGroupPrivilegeMapping(anyString(),anyString());
		_appService.Delete(userPermissionId); 
		verify(_userpermissionManager).Delete(userpermission);
	}
	

	
	@Test
	public void Find_ListIsEmpty_ReturnList() throws Exception {

		List<UserpermissionEntity> list = new ArrayList<>();
		Page<UserpermissionEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindUserpermissionByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");

		Mockito.when(_userpermissionManager.FindAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.Find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void Find_ListIsNotEmpty_ReturnList() throws Exception {

		List<UserpermissionEntity> list = new ArrayList<>();
		UserpermissionEntity userpermission = mock(UserpermissionEntity.class);
		list.add(userpermission);
    	Page<UserpermissionEntity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindUserpermissionByIdOutput> output = new ArrayList<>();
        SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");
		output.add(_mapper.UserpermissionEntityToFindUserpermissionByIdOutput(userpermission));
    	Mockito.when(_userpermissionManager.FindAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.Find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void searchAllProperties_SearchIsNotNull_ReturnBooleanBuilder() {
		String search= "xyz";
		String operator= "equals";
		QUserpermissionEntity userpermission = QUserpermissionEntity.userpermissionEntity;
		BooleanBuilder builder = new BooleanBuilder();
		Assertions.assertThat(_appService.searchAllProperties(userpermission,search,operator)).isEqualTo(builder);
	}
	
	@Test
	public void searchSpecificProperty_PropertyExists_ReturnBooleanBuilder() {
		String operator= "equals";
		List<String> list = new ArrayList<>();
		QUserpermissionEntity userpermission = QUserpermissionEntity.userpermissionEntity;
		BooleanBuilder builder = new BooleanBuilder();
		Assertions.assertThat(_appService.searchSpecificProperty(userpermission, list,"xyz",operator)).isEqualTo(builder);
	}
	
    @Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QUserpermissionEntity userpermission = QUserpermissionEntity.userpermissionEntity;
	    
	    SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue(String.valueOf(ID));
		Map<String,SearchFields> map = new HashMap<>();
		map.put("permissionId",searchFields);
		
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(userpermission.permission.id.eq(ID));
		
		Map<String,String> searchMap = new HashMap<String,String>();
		searchMap.put("permissionId",String.valueOf(ID));
		
		Assertions.assertThat(_appService.searchKeyValuePair(userpermission,map,searchMap)).isEqualTo(builder);
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

		SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");
		BooleanBuilder builder = new BooleanBuilder();
		Assertions.assertThat(_appService.Search(search)).isEqualTo(builder);
	}
	
	@Test
	public void  search_SearchIsNotNullAndSearchContainsCaseTwo_ReturnBooleanBuilder() throws Exception {

		QUserpermissionEntity userpermission = QUserpermissionEntity.userpermissionEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(2);
		search.setValue(String.valueOf(ID));
		search.setOperator("equals");
		
		fields.setFieldName("permissionId");
        fieldsList.add(fields);
        search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(userpermission.permission.id.eq(ID));
		Assertions.assertThat(_appService.Search(search)).isEqualTo(builder);
		
	}
	
    @Test
	public void  search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
		
		QUserpermissionEntity userpermission = QUserpermissionEntity.userpermissionEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue(String.valueOf(ID));
		search.setOperator("equals");
		
		Map<String,String> searchMap = new HashMap<String,String>();
		searchMap.put("permissionId",String.valueOf(ID));
		search.setJoinColumns(searchMap);

		fields.setOperator("equals");
		fields.setSearchValue(String.valueOf(ID));
		fields.setFieldName("permissionId");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
        
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(userpermission.permission.id.eq(ID));
		
		Assertions.assertThat(_appService.Search(search)).isEqualTo(builder);
		
	}
	
	@Test
	public void  search_StringIsNull_ReturnNull() throws Exception {

		Assertions.assertThat(_appService.Search(null)).isEqualTo(null);
	}
	
    //User
	@Test
	public void GetUser_IfUserpermissionIdAndUserIdIsNotNullAndUserpermissionExists_ReturnUser() {
		UserpermissionEntity userpermission = mock(UserpermissionEntity.class);
		UserEntity user = mock(UserEntity.class);

		Mockito.when(_userpermissionManager.FindById(any(UserpermissionId.class))).thenReturn(userpermission);
		Mockito.when(_userpermissionManager.GetUser(any(UserpermissionId.class))).thenReturn(user);
		Assertions.assertThat(_appService.GetUser(userPermissionId)).isEqualTo(_mapper.UserEntityToGetUserOutput(user, userpermission));
	}

	@Test 
	public void GetUser_IfUserpermissionIdAndUserIdIsNotNullAndUserpermissionDoesNotExist_ReturnNull() {
		
		Mockito.when(_userpermissionManager.FindById(any(UserpermissionId.class))).thenReturn(null);
		Assertions.assertThat(_appService.GetUser(userPermissionId)).isEqualTo(null);
	}
 
   //Permission
	@Test
	public void GetPermission_IfUserpermissionIdAndPermissionIdIsNotNullAndUserpermissionExists_ReturnPermission() {
		UserpermissionEntity userpermission = mock(UserpermissionEntity.class);
		PermissionEntity permission = mock(PermissionEntity.class);

		Mockito.when(_userpermissionManager.FindById(any(UserpermissionId.class))).thenReturn(userpermission);
		Mockito.when(_userpermissionManager.GetPermission(any(UserpermissionId.class))).thenReturn(permission);
		Assertions.assertThat(_appService.GetPermission(userPermissionId)).isEqualTo(_mapper.PermissionEntityToGetPermissionOutput(permission, userpermission));
	}

	@Test 
	public void GetPermission_IfUserpermissionIdAndPermissionIdIsNotNullAndUserpermissionDoesNotExist_ReturnNull() {
		
		Mockito.when(_userpermissionManager.FindById(any(UserpermissionId.class))).thenReturn(null);
		Assertions.assertThat(_appService.GetPermission(userPermissionId)).isEqualTo(null);
	}
	
	@Test
	public void ParseUserPermissionKey_KeysStringIsNotEmptyAndKeyValuePairExists_ReturnUserpermissionId()
	{
		String keyString= "userId:15,permissionId:15";
	
		UserpermissionId userpermissionId = new UserpermissionId();
		userpermissionId.setPermissionId(Long.valueOf(ID));

		Assertions.assertThat(_appService.parseUserpermissionKey(keyString)).isEqualToComparingFieldByField(userpermissionId);
	}
	
	@Test
	public void ParseUserPermissionKey_KeysStringIsEmpty_ReturnNull()
	{
		
		String keyString= "";
		Assertions.assertThat(_appService.parseUserpermissionKey(keyString)).isEqualTo(null);
	}
	
	@Test
	public void ParseUserPermissionKey_KeysStringIsNotEmptyAndKeyValuePairDoesNotExist_ReturnNull()
	{
		
		String keyString= "permissionId";

		Assertions.assertThat(_appService.parseUserpermissionKey(keyString)).isEqualTo(null);
	}
 

}

