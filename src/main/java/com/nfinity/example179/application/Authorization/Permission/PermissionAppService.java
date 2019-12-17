package com.nfinity.example179.application.Authorization.Permission;

import com.nfinity.example179.CommonModule.Search.SearchCriteria;
import com.nfinity.example179.CommonModule.Search.SearchFields;
import com.nfinity.example179.CommonModule.Search.SearchUtils;
import com.nfinity.example179.application.Authorization.Permission.Dto.*;
import com.nfinity.example179.domain.model.PermissionEntity;
import com.nfinity.example179.domain.Authorization.Permission.IPermissionManager;
import com.nfinity.example179.domain.model.QPermissionEntity;
import com.querydsl.core.BooleanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import com.nfinity.example179.application.Flowable.FlowableIdentityService;

@Service
@Validated
public class PermissionAppService implements IPermissionAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
    
    @Autowired
	private IPermissionManager _permissionManager;

	@Autowired
	private PermissionMapper mapper;

	@Autowired
	private FlowableIdentityService idmIdentityService;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreatePermissionOutput Create(CreatePermissionInput input) {

		PermissionEntity permission = mapper.CreatePermissionInputToPermissionEntity(input);
		PermissionEntity createdPermission = _permissionManager.Create(permission);
		idmIdentityService.createPrivilege(createdPermission.getName());
		return mapper.PermissionEntityToCreatePermissionOutput(createdPermission);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Permission", key = "#permissionId")
	public UpdatePermissionOutput Update(Long permissionId, UpdatePermissionInput input) {
		String oldPermissionName = null;
		PermissionEntity oldPermission = _permissionManager.FindById(permissionId);
		if(oldPermission != null) {
			oldPermissionName = oldPermission.getName();
		}
		PermissionEntity permission = mapper.UpdatePermissionInputToPermissionEntity(input);
		PermissionEntity updatedPermission = _permissionManager.Update(permission);
		idmIdentityService.updatePrivilege(oldPermissionName, updatedPermission.getName());
		return mapper.PermissionEntityToUpdatePermissionOutput(updatedPermission);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Permission", key = "#permissionId")
	public void Delete(Long permissionId) {

		PermissionEntity existing = _permissionManager.FindById(permissionId) ; 
		_permissionManager.Delete(existing);
		idmIdentityService.deletePrivilege(existing.getName());
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Permission", key = "#permissionId")
	public FindPermissionByIdOutput FindById(Long permissionId) {

		PermissionEntity foundPermission = _permissionManager.FindById(permissionId);
		if (foundPermission == null)  
			return null ; 
 	   
 	   FindPermissionByIdOutput output=mapper.PermissionEntityToFindPermissionByIdOutput(foundPermission); 
		return output;
	}

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable(value = "Permission", key = "#permissionName")
	public FindPermissionByNameOutput FindByPermissionName(String permissionName) {

		PermissionEntity foundPermission = _permissionManager.FindByPermissionName(permissionName);
		if (foundPermission == null) {
			return null;
		}
		return mapper.PermissionEntityToFindPermissionByNameOutput(foundPermission);

	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Permission")
	public List<FindPermissionByIdOutput> Find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<PermissionEntity> foundPermission = _permissionManager.FindAll(Search(search), pageable);
		List<PermissionEntity> permissionList = foundPermission.getContent();
		Iterator<PermissionEntity> permissionIterator = permissionList.iterator(); 
		List<FindPermissionByIdOutput> output = new ArrayList<>();

		while (permissionIterator.hasNext()) {
			output.add(mapper.PermissionEntityToFindPermissionByIdOutput(permissionIterator.next()));
		}
		return output;
	}
	
	BooleanBuilder Search(SearchCriteria search) throws Exception {

		QPermissionEntity permission= QPermissionEntity.permissionEntity;
		if(search != null) {
			if(search.getType()==case1)
			{
				return searchAllProperties(permission, search.getValue(),search.getOperator());
			}
			else if(search.getType()==case2)
			{
				List<String> keysList = new ArrayList<String>();
				for(SearchFields f: search.getFields())
				{
					keysList.add(f.getFieldName());
				}
				checkProperties(keysList);
				return searchSpecificProperty(permission,keysList,search.getValue(),search.getOperator());
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
				return searchKeyValuePair(permission, map,search.getJoinColumns());
			}

		}
		return null;
	}
	
	BooleanBuilder searchAllProperties(QPermissionEntity permission,String value,String operator) {
		BooleanBuilder builder = new BooleanBuilder();

		if(operator.equals("contains")) {
			builder.or(permission.displayName.likeIgnoreCase("%"+ value + "%"));
			builder.or(permission.name.likeIgnoreCase("%"+ value + "%"));
		}
		else if(operator.equals("equals"))
		{
        	builder.or(permission.displayName.eq(value));
        	builder.or(permission.name.eq(value));
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
		 list.get(i).replace("%20","").trim().equals("userpermission")
		)) 
		{
		 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
		}
		}
	}
	
	BooleanBuilder searchSpecificProperty(QPermissionEntity permission,List<String> list,String value,String operator)  {
		BooleanBuilder builder = new BooleanBuilder();
		
		for (int i = 0; i < list.size(); i++) {
		
            if(list.get(i).replace("%20","").trim().equals("displayName")) {
				if(operator.equals("contains"))
					builder.or(permission.displayName.likeIgnoreCase("%"+ value + "%"));
				else if(operator.equals("equals"))
					builder.or(permission.displayName.eq(value));
			}
            if(list.get(i).replace("%20","").trim().equals("name")) {
				if(operator.equals("contains"))
					builder.or(permission.name.likeIgnoreCase("%"+ value + "%"));
				else if(operator.equals("equals"))
					builder.or(permission.name.eq(value));
			}
		}
		return builder;
	}
	
	BooleanBuilder searchKeyValuePair(QPermissionEntity permission, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
            if(details.getKey().replace("%20","").trim().equals("displayName")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(permission.displayName.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(permission.displayName.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(permission.displayName.ne(details.getValue().getSearchValue()));
			}
            if(details.getKey().replace("%20","").trim().equals("name")) {
				if(details.getValue().getOperator().equals("contains"))
					builder.and(permission.name.likeIgnoreCase("%"+ details.getValue().getSearchValue() + "%"));
				else if(details.getValue().getOperator().equals("equals"))
					builder.and(permission.name.eq(details.getValue().getSearchValue()));
				else if(details.getValue().getOperator().equals("notEqual"))
					builder.and(permission.name.ne(details.getValue().getSearchValue()));
			}
		}
		return builder;
	}
	
	public Map<String,String> parseRolepermissionJoinColumn(String keysString) {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("permissionId", keysString);
		return joinColumnMap;
		
	}
	
	
	public Map<String,String> parseUserpermissionJoinColumn(String keysString) {
		
		Map<String,String> joinColumnMap = new HashMap<String,String>();
		joinColumnMap.put("permissionId", keysString);
		return joinColumnMap;
		
	}
}
