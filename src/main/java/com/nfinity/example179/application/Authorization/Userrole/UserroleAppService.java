package com.nfinity.example179.application.Authorization.Userrole;

import com.nfinity.example179.application.Authorization.Userrole.Dto.*;
import com.nfinity.example179.domain.Authorization.Userrole.IUserroleManager;
import com.nfinity.example179.domain.model.QUserroleEntity;
import com.nfinity.example179.domain.model.UserroleEntity;
import com.nfinity.example179.domain.model.UserroleId;
import com.nfinity.example179.domain.Authorization.User.UserManager;
import com.nfinity.example179.domain.model.UserEntity;
import com.nfinity.example179.domain.Authorization.Role.RoleManager;
import com.nfinity.example179.domain.model.RoleEntity;
import com.nfinity.example179.CommonModule.Search.*;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;
import com.nfinity.example179.application.Flowable.FlowableIdentityService;
import com.querydsl.core.BooleanBuilder;

import java.util.*;
import org.springframework.cache.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.*;

@Service
@Validated
public class UserroleAppService implements IUserroleAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IUserroleManager _userroleManager;
	
    @Autowired
	private UserManager _userManager;
    
    @Autowired
	private RoleManager _roleManager;
    
	@Autowired
	private UserroleMapper mapper;
	
	@Autowired
	private LoggingHelper logHelper;
	
	@Autowired
	private FlowableIdentityService idmIdentityService;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateUserroleOutput Create(CreateUserroleInput input) {

		UserroleEntity userrole = mapper.CreateUserroleInputToUserroleEntity(input);
	  	if(input.getUserId()!=null || input.getRoleId()!=null)
	  	{
			UserEntity foundUser = _userManager.FindById(input.getUserId());
	        RoleEntity foundRole = _roleManager.FindById(input.getRoleId());
		
		    if(foundUser!=null || foundRole!=null)
		    {			
				if(!checkIfRoleAlreadyAssigned(foundUser, foundRole))
				{
					userrole.setRole(foundRole);
					userrole.setUser(foundUser);
				}
				else return null;
		     }
		     else return null;
		}
		else return null;
		
		UserroleEntity createdUserrole = _userroleManager.Create(userrole);
		idmIdentityService.addUserGroupMapping(createdUserrole.getUser().getUserName(), createdUserrole.getRole().getName());
		
		return mapper.UserroleEntityToCreateUserroleOutput(createdUserrole);
	
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Userrole", key = "#userroleId")
	public UpdateUserroleOutput Update(UserroleId userroleId , UpdateUserroleInput input) {

		UserroleEntity userrole = mapper.UpdateUserroleInputToUserroleEntity(input);
	  	if(input.getUserId()!=null || input.getRoleId()!=null)
	  	{
			UserEntity foundUser = _userManager.FindById(input.getUserId());
	        RoleEntity foundRole = _roleManager.FindById(input.getRoleId());
		
		    if(foundUser!=null || foundRole!=null)
		    {			
				if(checkIfRoleAlreadyAssigned(foundUser, foundRole))
				{
					userrole.setRole(foundRole);
					userrole.setUser(foundUser);
				}
				else return null;
		     }
		     else return null;
		}
		else return null;
		
		UserroleEntity updatedUserrole = _userroleManager.Create(userrole);
		idmIdentityService.updateUserGroupMapping(updatedUserrole.getUser().getUserName(), updatedUserrole.getRole().getName());
		return mapper.UserroleEntityToUpdateUserroleOutput(updatedUserrole);
	}
	
	public boolean checkIfRoleAlreadyAssigned(UserEntity foundUser,RoleEntity foundRole)
	{
	
		Set<UserroleEntity> userRole = foundUser.getUserroleSet();
		 
		Iterator rIterator = userRole.iterator();
			while (rIterator.hasNext()) { 
				UserroleEntity ur = (UserroleEntity) rIterator.next();
				if (ur.getRole() == foundRole) {
					return true;
				}
			}
			
		return false;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Userrole", key = "#userroleId")
	public void Delete(UserroleId userroleId) {

		UserroleEntity existing = _userroleManager.FindById(userroleId) ; 
		_userroleManager.Delete(existing);
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Userrole", key = "#userroleId")
	public FindUserroleByIdOutput FindById(UserroleId userroleId) {

		UserroleEntity foundUserrole = _userroleManager.FindById(userroleId);
		if (foundUserrole == null)  
			return null ; 
 	   
 	    FindUserroleByIdOutput output=mapper.UserroleEntityToFindUserroleByIdOutput(foundUserrole); 
		return output;
	}
    //User
	// ReST API Call - GET /userrole/1/user
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable (value = "Userrole", key="#userroleId")
	public GetUserOutput GetUser(UserroleId userroleId) {

		UserroleEntity foundUserrole = _userroleManager.FindById(userroleId);
		if (foundUserrole == null) {
			logHelper.getLogger().error("There does not exist a userrole wth a id=%s", userroleId);
			return null;
		}
		UserEntity re = _userroleManager.GetUser(userroleId);
		return mapper.UserEntityToGetUserOutput(re, foundUserrole);
	}
	
    //Role
	// ReST API Call - GET /userrole/1/role
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable (value = "Userrole", key="#userroleId")
	public GetRoleOutput GetRole(UserroleId userroleId) {

		UserroleEntity foundUserrole = _userroleManager.FindById(userroleId);
		if (foundUserrole == null) {
			logHelper.getLogger().error("There does not exist a userrole wth a id=%s", userroleId);
			return null;
		}
		RoleEntity re = _userroleManager.GetRole(userroleId);
		return mapper.RoleEntityToGetRoleOutput(re, foundUserrole);
	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Userrole")
	public List<FindUserroleByIdOutput> Find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<UserroleEntity> foundUserrole = _userroleManager.FindAll(Search(search), pageable);
		List<UserroleEntity> userroleList = foundUserrole.getContent();
		Iterator<UserroleEntity> userroleIterator = userroleList.iterator(); 
		List<FindUserroleByIdOutput> output = new ArrayList<>();

		while (userroleIterator.hasNext()) {
			output.add(mapper.UserroleEntityToFindUserroleByIdOutput(userroleIterator.next()));
		}
		return output;
	}
	
	BooleanBuilder Search(SearchCriteria search) throws Exception {

		QUserroleEntity userrole= QUserroleEntity.userroleEntity;
		if(search != null) {
			if(search.getType()==case1)
			{
				return searchAllProperties(userrole, search.getValue(),search.getOperator());
			}
			else if(search.getType()==case2)
			{
				List<String> keysList = new ArrayList<String>();
				for(SearchFields f: search.getFields())
				{
					keysList.add(f.getFieldName());
				}
				checkProperties(keysList);
				return searchSpecificProperty(userrole,keysList,search.getValue(),search.getOperator());
			}
			else if(search.getType()==case3)
			{
				Map<String,SearchFields> map = new HashMap<>();
				for(SearchFields fieldDetails: search.getFields())
				{
					map.put(fieldDetails.getFieldName(),fieldDetails);
				}
				List<String> keysList = new ArrayList<String>(map.keySet());
				checkProperties(keysList);
				return searchKeyValuePair(userrole, map,search.getJoinColumns());
			}

		}
		return null;
	}
	
	BooleanBuilder searchAllProperties(QUserroleEntity userrole,String value,String operator) {
		BooleanBuilder builder = new BooleanBuilder();

		if(operator.equals("contains")) {
		}
		else if(operator.equals("equals"))
		{
        	if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
       	 	}
			else if(StringUtils.isNumeric(value)){
        	}
        	else if(SearchUtils.stringToDate(value)!=null) {
			}
		}

		return builder;
	}

	public void checkProperties(List<String> list) throws Exception  {
		for (int i = 0; i < list.size(); i++) {
		if(!(
    	list.get(i).replace("%20","").trim().equals("userId")||
		list.get(i).replace("%20","").trim().equals("role") ||
		list.get(i).replace("%20","").trim().equals("roleId") ||
		list.get(i).replace("%20","").trim().equals("user"))) 
		{
		 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
		}
		}
	}
	
	BooleanBuilder searchSpecificProperty(QUserroleEntity userrole,List<String> list,String value,String operator)  {
		BooleanBuilder builder = new BooleanBuilder();
		
		for (int i = 0; i < list.size(); i++) {
		if(list.get(i).replace("%20","").trim().equals("userId")) {
			builder.or(userrole.user.id.eq(Long.parseLong(value)));
		}
		if(list.get(i).replace("%20","").trim().equals("roleId")) {
			builder.or(userrole.role.id.eq(Long.parseLong(value)));
		}
		}
		return builder;
	}
	
	BooleanBuilder searchKeyValuePair(QUserroleEntity userrole, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();
        
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("userId")) {
		    builder.and(userrole.user.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("roleId")) {
		    builder.and(userrole.role.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		return builder;
	}
	
	public UserroleId parseUserroleKey(String keysString) {
		
		String[] keyEntries = keysString.split(",");
		UserroleId userroleId = new UserroleId();
		
		Map<String,String> keyMap = new HashMap<String,String>();
		if(keyEntries.length > 1) {
			for(String keyEntry: keyEntries)
			{
				String[] keyEntryArr = keyEntry.split(":");
				if(keyEntryArr.length > 1) {
					keyMap.put(keyEntryArr[0], keyEntryArr[1]);					
				}
				else {
					return null;
				}
			}
		}
		else {
			return null;
		}
		
		userroleId.setRoleId(Long.valueOf(keyMap.get("roleId")));
        userroleId.setUserId(Long.valueOf(keyMap.get("userId")));
		return userroleId;
		
	}	
	
    
	
}


