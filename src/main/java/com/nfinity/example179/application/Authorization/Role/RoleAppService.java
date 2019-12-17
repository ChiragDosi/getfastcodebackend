package com.nfinity.example179.application.Authorization.Role;

import com.nfinity.example179.CommonModule.Search.SearchCriteria;
import com.nfinity.example179.CommonModule.Search.SearchFields;
import com.nfinity.example179.CommonModule.Search.SearchUtils;
import com.nfinity.example179.application.Authorization.Role.Dto.*;
import com.nfinity.example179.domain.Authorization.Role.IRoleManager;
import com.nfinity.example179.domain.model.RoleEntity;
import com.nfinity.example179.domain.model.QRoleEntity;
import com.querydsl.core.BooleanBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.*;
import org.apache.commons.lang3.StringUtils;
import com.nfinity.example179.application.Flowable.ActIdGroupMapper;
import com.nfinity.example179.application.Flowable.FlowableIdentityService;

@Service
@Validated
public class RoleAppService implements IRoleAppService{

	static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IRoleManager _roleManager;

	@Autowired
	private RoleMapper mapper;

		@Autowired
		private FlowableIdentityService idmIdentityService;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateRoleOutput Create(CreateRoleInput input) {

		RoleEntity role = mapper.CreateRoleInputToRoleEntity(input);
		RoleEntity createdRole = _roleManager.Create(role);

		//Map and create flowable groupS
		idmIdentityService.createGroup(createdRole.getName());

		return mapper.RoleEntityToCreateRoleOutput(createdRole);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Role", key = "#p0")
	public UpdateRoleOutput Update(Long roleId, UpdateRoleInput input) {

		String oldRoleName = null;
		RoleEntity oldUserRole = _roleManager.FindById(roleId);
		if(oldUserRole != null) {
			oldRoleName = oldUserRole.getName();
		}
		RoleEntity role = mapper.UpdateRoleInputToRoleEntity(input);
		RoleEntity updatedRole = _roleManager.Update(role);
		//Map and delete flowable groupS
		idmIdentityService.updateGroup(updatedRole.getName(), oldRoleName);
		return mapper.RoleEntityToUpdateRoleOutput(updatedRole);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Role", key = "#p0")
	public void Delete(Long roleId) {

		RoleEntity existing = _roleManager.FindById(roleId) ; 
		_roleManager.Delete(existing);
		//Map and delete flowable groupS
		idmIdentityService.deleteGroup(existing.getName());
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Role", key = "#p0")
	public FindRoleByIdOutput FindById(Long roleId) {

		RoleEntity foundRole = _roleManager.FindById(roleId);
		if (foundRole == null)  
			return null ; 
 	   
 	   FindRoleByIdOutput output=mapper.RoleEntityToFindRoleByIdOutput(foundRole); 
		return output;
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable(value = "Role", key = "#p0")
	public FindRoleByNameOutput FindByRoleName(String roleName) {

		RoleEntity foundRole = _roleManager.FindByRoleName(roleName);

		if (foundRole == null) {
			return null;
		}
		return mapper.RoleEntityToFindRoleByNameOutput(foundRole);
	}

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Role")
	public List<FindRoleByIdOutput> Find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<RoleEntity> foundRole = _roleManager.FindAll(Search(search), pageable);
		List<RoleEntity> roleList = foundRole.getContent();
		Iterator<RoleEntity> roleIterator = roleList.iterator(); 
		List<FindRoleByIdOutput> output = new ArrayList<>();

		while (roleIterator.hasNext()) {
			output.add(mapper.RoleEntityToFindRoleByIdOutput(roleIterator.next()));
		}
		return output;
	}
	
	BooleanBuilder Search(SearchCriteria search) throws Exception {

		QRoleEntity role= QRoleEntity.roleEntity;
		if(search != null) {
			if(search.getType()==case1)
			{
				return searchAllProperties(role, search.getValue(),search.getOperator());
			}
			else if(search.getType()==case2)
			{
				List<String> keysList = new ArrayList<String>();
				for(SearchFields f: search.getFields())
				{
					keysList.add(f.getFieldName());
				}
				checkProperties(keysList);
				return searchSpecificProperty(role,keysList,search.getValue(),search.getOperator());
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
				return searchKeyValuePair(role, map,search.getJoinColumns());
			}

		}
		return null;
	}
	
	BooleanBuilder searchAllProperties(QRoleEntity role,String value,String operator) {
		BooleanBuilder builder = new BooleanBuilder();

		if(operator.equals("contains")) {
			builder.or(role.displayName.likeIgnoreCase("%"+ value + "%"));
			builder.or(role.name.likeIgnoreCase("%"+ value + "%"));
		}
		else if(operator.equals("equals"))
		{
        	builder.or(role.displayName.eq(value));
        	builder.or(role.name.eq(value));
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
		
		 list.get(i).replace("%20","").trim().equals("displayName") ||
		 list.get(i).replace("%20","").trim().equals("id") ||
		 list.get(i).replace("%20","").trim().equals("name") ||
		 list.get(i).replace("%20","").trim().equals("rolepermission") ||
		 list.get(i).replace("%20","").trim().equals("user")
		)) 
		{
		 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
		}
		}
	}
	
	BooleanBuilder searchSpecificProperty(QRoleEntity role,List<String> list,String value,String operator)  {
		BooleanBuilder builder = new BooleanBuilder();
		
		for (int i = 0; i < list.size(); i++) {
		
            if(list.get(i).replace("%20","").trim().equals("displayName")) {
				if(operator.equals("contains"))
					builder.or(role.displayName.likeIgnoreCase("%"+ value + "%"));
				else if(operator.equals("equals"))
					builder.or(role.displayName.eq(value));
			}
            if(list.get(i).replace("%20","").trim().equals("name")) {
				if(operator.equals("contains"))
					builder.or(role.name.likeIgnoreCase("%"+ value + "%"));
				else if(operator.equals("equals"))
					builder.or(role.name.eq(value));
			}
		}
		return builder;
	}
	
	BooleanBuilder searchKeyValuePair(QRoleEntity role, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if(details.getKey().replace("%20","").trim().equals("displayName")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(role.displayName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(role.displayName.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(role.displayName.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("name")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(role.name.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(role.name.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(role.name.ne(details.getValue().getSearchValue()));
			}
		}
		return builder;
	}

	public Map<String,String> parseRolepermissionJoinColumn(String keysString) {
	
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("roleId", keysString);
		return joinColumnMap;
	}

	public Map<String,String> parseUserroleJoinColumn(String keysString) {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("roleId", keysString);
		return joinColumnMap;
	}
	
}