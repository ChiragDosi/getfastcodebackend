package com.nfinity.example179.application.Authorization.Rolepermission;

import com.nfinity.example179.application.Authorization.Rolepermission.Dto.*;
import com.nfinity.example179.domain.Authorization.Rolepermission.IRolepermissionManager;
import com.nfinity.example179.domain.model.QRolepermissionEntity;
import com.nfinity.example179.domain.model.RolepermissionEntity;
import com.nfinity.example179.domain.model.RolepermissionId;
import com.nfinity.example179.domain.Authorization.Permission.IPermissionManager;
import com.nfinity.example179.domain.model.PermissionEntity;
import com.nfinity.example179.domain.Authorization.Role.IRoleManager;
import com.nfinity.example179.domain.model.RoleEntity;
import com.nfinity.example179.CommonModule.Search.*;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import org.springframework.cache.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.nfinity.example179.application.Flowable.FlowableIdentityService;

@Service
@Validated
public class RolepermissionAppService implements IRolepermissionAppService {

    static final int case1=1;
	static final int case2=2;
	static final int case3=3;
	
	@Autowired
	private IRolepermissionManager _rolepermissionManager;
  
    @Autowired
	private IPermissionManager _permissionManager;
    
    @Autowired
	private IRoleManager _roleManager;
    
	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private RolepermissionMapper mapper;

	@Autowired
	private FlowableIdentityService idmIdentityService;

    @Transactional(propagation = Propagation.REQUIRED)
	public CreateRolepermissionOutput Create(CreateRolepermissionInput input) {

		RolepermissionEntity rolepermission = mapper.CreateRolepermissionInputToRolepermissionEntity(input);
		if(input.getPermissionId()!=null && input.getRoleId()!=null){
			PermissionEntity foundPermission = _permissionManager.FindById(input.getPermissionId());
			RoleEntity foundRole = _roleManager.FindById(input.getRoleId());
			
			if(foundPermission!=null && foundRole!=null) {
				rolepermission.setPermission(foundPermission);
				rolepermission.setRole(foundRole);
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
		RolepermissionEntity createdRolepermission = _rolepermissionManager.Create(rolepermission);
		idmIdentityService.addGroupPrivilegeMapping(rolepermission.getRole().getName(), rolepermission.getPermission().getName());
		return mapper.RolepermissionEntityToCreateRolepermissionOutput(createdRolepermission);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Rolepermission", key = "#rolepermissionId")
	public UpdateRolepermissionOutput Update(RolepermissionId rolepermissionId , UpdateRolepermissionInput input) {

		RolepermissionEntity rolepermission = mapper.UpdateRolepermissionInputToRolepermissionEntity(input);

		if(input.getPermissionId()!=null && input.getRoleId()!=null){
			PermissionEntity foundPermission = _permissionManager.FindById(input.getPermissionId());
			RoleEntity foundRole = _roleManager.FindById(input.getRoleId());

			if(foundPermission!=null && foundRole!=null) {
				rolepermission.setPermission(foundPermission);
				rolepermission.setRole(foundRole);
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
		
		RolepermissionEntity updatedRolepermission = _rolepermissionManager.Update(rolepermission);
		idmIdentityService.updateGroupPrivilegeMapping(updatedRolepermission.getRole().getName(), updatedRolepermission.getPermission().getName());
        
		return mapper.RolepermissionEntityToUpdateRolepermissionOutput(updatedRolepermission);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	@CacheEvict(value="Rolepermission", key = "#rolepermissionId")
	public void Delete(RolepermissionId rolepermissionId ) {

		RolepermissionEntity existing = _rolepermissionManager.FindById(rolepermissionId) ; 
		_rolepermissionManager.Delete(existing);
		RoleEntity foundRole = _roleManager.FindById(existing.getRoleId());
		PermissionEntity foundPermission = _permissionManager.FindById(existing.getPermissionId());
		idmIdentityService.deleteGroupPrivilegeMapping(foundRole.getName(), foundPermission.getName());
	}
	
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Rolepermission", key = "#rolepermissionId")
	public FindRolepermissionByIdOutput FindById(RolepermissionId rolepermissionId) {

		RolepermissionEntity foundRolepermission = _rolepermissionManager.FindById(rolepermissionId);
		if (foundRolepermission == null)  
			return null ; 
 	   
 	   FindRolepermissionByIdOutput output=mapper.RolepermissionEntityToFindRolepermissionByIdOutput(foundRolepermission); 
		return output;
	}
    //Permission
	// ReST API Call - GET /rolepermission/1/permission
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable (value = "Rolepermission", key="#rolepermissionId")
	public GetPermissionOutput GetPermission(RolepermissionId rolepermissionId ) {

		RolepermissionEntity foundRolepermission = _rolepermissionManager.FindById(rolepermissionId);
		if (foundRolepermission == null) {
			logHelper.getLogger().error("There does not exist a rolepermission wth a id=%s", rolepermissionId);
			return null;
		}
		PermissionEntity re = _rolepermissionManager.GetPermission(rolepermissionId);
		return mapper.PermissionEntityToGetPermissionOutput(re, foundRolepermission);
	}
    
    //Role
	// ReST API Call - GET /rolepermission/1/role
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Cacheable (value = "Rolepermission", key="#rolepermissionId")
	public GetRoleOutput GetRole(RolepermissionId rolepermissionId ) {

		RolepermissionEntity foundRolepermission = _rolepermissionManager.FindById(rolepermissionId);
		if (foundRolepermission == null) {
			logHelper.getLogger().error("There does not exist a rolepermission wth a id=%s", rolepermissionId);
			return null;
		}
		RoleEntity re = _rolepermissionManager.GetRole(rolepermissionId);
		return mapper.RoleEntityToGetRoleOutput(re, foundRolepermission);
	}
    

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Cacheable(value = "Rolepermission")
	public List<FindRolepermissionByIdOutput> Find(SearchCriteria search, Pageable pageable) throws Exception  {

		Page<RolepermissionEntity> foundRolepermission = _rolepermissionManager.FindAll(Search(search), pageable);
		List<RolepermissionEntity> rolepermissionList = foundRolepermission.getContent();
		Iterator<RolepermissionEntity> rolepermissionIterator = rolepermissionList.iterator(); 
		List<FindRolepermissionByIdOutput> output = new ArrayList<>();

		while (rolepermissionIterator.hasNext()) {
			output.add(mapper.RolepermissionEntityToFindRolepermissionByIdOutput(rolepermissionIterator.next()));
		}
		return output;
	}
	
	BooleanBuilder Search(SearchCriteria search) throws Exception {

		QRolepermissionEntity rolepermission= QRolepermissionEntity.rolepermissionEntity;
		if(search != null) {
			if(search.getType()==case1)
			{
				return searchAllProperties(rolepermission, search.getValue(),search.getOperator());
			}
			else if(search.getType()==case2)
			{
				List<String> keysList = new ArrayList<String>();
				for(SearchFields f: search.getFields())
				{
					keysList.add(f.getFieldName());
				}
				checkProperties(keysList);
				return searchSpecificProperty(rolepermission,keysList,search.getValue(),search.getOperator());
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
				return searchKeyValuePair(rolepermission, map,search.getJoinColumns());
			}

		}
		return null;
	}
	
	BooleanBuilder searchAllProperties(QRolepermissionEntity rolepermission,String value,String operator) {
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
		
		 list.get(i).replace("%20","").trim().equals("permission") ||
		 list.get(i).replace("%20","").trim().equals("permissionId") ||
		 list.get(i).replace("%20","").trim().equals("role") ||
		 list.get(i).replace("%20","").trim().equals("roleId")
		)) 
		{
		 throw new Exception("Wrong URL Format: Property " + list.get(i) + " not found!" );
		}
		}
	}
	
	BooleanBuilder searchSpecificProperty(QRolepermissionEntity rolepermission,List<String> list,String value,String operator)  {
		BooleanBuilder builder = new BooleanBuilder();
		
		for (int i = 0; i < list.size(); i++) {
		
		  if(list.get(i).replace("%20","").trim().equals("permissionId")) {
			builder.or(rolepermission.permission.id.eq(Long.parseLong(value)));
			}
		  if(list.get(i).replace("%20","").trim().equals("roleId")) {
			builder.or(rolepermission.role.id.eq(Long.parseLong(value)));
			}
		}
		return builder;
	}
	
	BooleanBuilder searchKeyValuePair(QRolepermissionEntity rolepermission, Map<String,SearchFields> map,Map<String,String> joinColumns) {
		BooleanBuilder builder = new BooleanBuilder();

		for (Map.Entry<String, SearchFields> details : map.entrySet()) {
		}
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("permissionId")) {
		    builder.and(rolepermission.permission.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		for (Map.Entry<String, String> joinCol : joinColumns.entrySet()) {
        if(joinCol != null && joinCol.getKey().equals("roleId")) {
		    builder.and(rolepermission.role.id.eq(Long.parseLong(joinCol.getValue())));
		}
        }
		return builder;
	}
	
	public RolepermissionId parseRolepermissionKey(String keysString) {
		
		String[] keyEntries = keysString.split(",");
		RolepermissionId rolepermissionId = new RolepermissionId();
		
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
		
		rolepermissionId.setPermissionId(Long.valueOf(keyMap.get("permissionId")));
		rolepermissionId.setRoleId(Long.valueOf(keyMap.get("roleId")));
		return rolepermissionId;
		
	}	
	
	
}


