package com.nfinity.example179.RestControllers;

import com.nfinity.example179.application.Authorization.Permission.PermissionAppService;
import com.nfinity.example179.application.Authorization.Rolepermission.RolepermissionAppService;
import com.nfinity.example179.application.Authorization.Userpermission.UserpermissionAppService;
import com.nfinity.example179.application.Authorization.Userpermission.Dto.FindUserpermissionByIdOutput;
import com.nfinity.example179.application.Authorization.Permission.Dto.*;
import com.nfinity.example179.application.Authorization.Rolepermission.Dto.FindRolepermissionByIdOutput;
import com.nfinity.example179.CommonModule.Search.SearchCriteria;
import com.nfinity.example179.CommonModule.Search.SearchUtils;
import com.nfinity.example179.CommonModule.application.OffsetBasedPageRequest;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;
import com.nfinity.example179.CommonModule.domain.EmptyJsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/permission")
public class PermissionController {

	@Autowired
	private PermissionAppService _permissionAppService;
    
    @Autowired
	private RolepermissionAppService  _rolepermissionAppService;
    @Autowired
	private UserpermissionAppService  _userpermissionAppService;
	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;

	// CRUD Operations

	// ------------ Create a permission ------------
	@PreAuthorize("hasAnyAuthority('PERMISSIONENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreatePermissionOutput> Create(@RequestBody @Valid CreatePermissionInput permission) {

		FindPermissionByNameOutput existing = _permissionAppService.FindByPermissionName(permission.getName());
        
        if (existing != null) {
            logHelper.getLogger().error("There already exists a permission with name=%s", permission.getName());
            throw new EntityExistsException(
                    String.format("There already exists a permission with name=%s", permission.getName()));
        }
        
		CreatePermissionOutput output=_permissionAppService.Create(permission);
		return new ResponseEntity(output, HttpStatus.OK);
	}

	// ------------ Delete permission ------------
	@PreAuthorize("hasAnyAuthority('PERMISSIONENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void Delete(@PathVariable String id) {
    FindPermissionByIdOutput output = _permissionAppService.FindById(Long.valueOf(id));
	
	if (output == null) {
		logHelper.getLogger().error("There does not exist a permission with a id=%s", id);
		throw new EntityNotFoundException(
			String.format("There does not exist a permission with a id=%s", id));
	}
    _permissionAppService.Delete(Long.valueOf(id));
    }
	
	// ------------ Update permission ------------
	@PreAuthorize("hasAnyAuthority('PERMISSIONENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdatePermissionOutput> Update(@PathVariable String id, @RequestBody @Valid UpdatePermissionInput permission) {
    FindPermissionByIdOutput currentPermission = _permissionAppService.FindById(Long.valueOf(id));
		
		if (currentPermission == null) {
			logHelper.getLogger().error("Unable to update. Permission with id {} not found.", id);
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
    return new ResponseEntity(_permissionAppService.Update(Long.valueOf(id),permission), HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('PERMISSIONENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindPermissionByIdOutput> FindById(@PathVariable String id) {
    FindPermissionByIdOutput output = _permissionAppService.FindById(Long.valueOf(id));
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('PERMISSIONENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity Find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }
//		if (sort.isUnsorted()) { sort = new Sort(Sort.Direction.fromString(env.getProperty("fastCode.sort.direction.default")), new String[]{env.getProperty("fastCode.sort.property.default")}); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		return ResponseEntity.ok(_permissionAppService.Find(searchCriteria,Pageable));
	}
    
    @PreAuthorize("hasAnyAuthority('PERMISSIONENTITY_READ')")
	@RequestMapping(value = "/{permissionid}/rolepermission", method = RequestMethod.GET)
	public ResponseEntity GetRolepermission(@PathVariable String permissionid, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }
//		if (sort.isUnsorted()) { sort = new Sort(Sort.Direction.fromString(env.getProperty("fastCode.sort.direction.default")), new String[]{env.getProperty("fastCode.sort.property.default")}); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_permissionAppService.parseRolepermissionJoinColumn(permissionid);
		if(joinColDetails== null)
		{
			logHelper.getLogger().error("Invalid Join Column");
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		searchCriteria.setJoinColumns(joinColDetails);
		
    	List<FindRolepermissionByIdOutput> output = _rolepermissionAppService.Find(searchCriteria,pageable);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyAuthority('PERMISSIONENTITY_READ')")
	@RequestMapping(value = "/{permissionid}/userpermission", method = RequestMethod.GET)
	public ResponseEntity GetUserpermission(@PathVariable String permissionid, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }
//		if (sort.isUnsorted()) { sort = new Sort(Sort.Direction.fromString(env.getProperty("fastCode.sort.direction.default")), new String[]{env.getProperty("fastCode.sort.property.default")}); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_permissionAppService.parseUserpermissionJoinColumn(permissionid);
		if(joinColDetails== null)
		{
			logHelper.getLogger().error("Invalid Join Column");
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		searchCriteria.setJoinColumns(joinColDetails);
		
    	List<FindUserpermissionByIdOutput> output = _userpermissionAppService.Find(searchCriteria,pageable);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	} 
}
