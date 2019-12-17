package com.nfinity.example179.RestControllers;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.nfinity.example179.domain.model.UserpermissionId;
import com.nfinity.example179.CommonModule.Search.SearchCriteria;
import com.nfinity.example179.CommonModule.Search.SearchUtils;
import com.nfinity.example179.CommonModule.application.OffsetBasedPageRequest;
import com.nfinity.example179.CommonModule.domain.EmptyJsonResponse;
import com.nfinity.example179.application.Authorization.User.UserAppService;
import com.nfinity.example179.application.Authorization.Userpermission.UserpermissionAppService;
import com.nfinity.example179.application.Authorization.Userpermission.Dto.*;
import com.nfinity.example179.application.Authorization.User.Dto.*;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;

@RestController
@RequestMapping("/userpermission")
public class UserpermissionController {

	@Autowired
	private UserpermissionAppService _userpermissionAppService;
	
	@Autowired
	private UserAppService _userAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;
    
    @PreAuthorize("hasAnyAuthority('USERPERMISSIONENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateUserpermissionOutput> Create(@RequestBody @Valid CreateUserpermissionInput userpermission) {
		CreateUserpermissionOutput output=_userpermissionAppService.Create(userpermission);
		if(output==null)
		{
			logHelper.getLogger().error("No record found");
		throw new EntityNotFoundException(
				String.format("No record found"));
	    }
	    
		FindUserByIdOutput foundUser =_userAppService.FindById(output.getUserId());
	   _userAppService.deleteAllUserTokens(foundUser.getUserName());  
		
		return new ResponseEntity(output, HttpStatus.OK);
	}

	// ------------ Delete userrpermission ------------
	@PreAuthorize("hasAnyAuthority('USERPERMISSIONENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void Delete(@PathVariable String id) {
	UserpermissionId userpermissionId =_userpermissionAppService.parseUserpermissionKey(id);
	if(userpermissionId == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	FindUserpermissionByIdOutput output = _userpermissionAppService.FindById(userpermissionId);
	if (output == null) {
		logHelper.getLogger().error("There does not exist a userpermission with a id=%s", id);
		throw new EntityNotFoundException(
			String.format("There does not exist a userpermission with a id=%s", id));
	}
	 _userpermissionAppService.Delete(userpermissionId);
	 
	 FindUserByIdOutput foundUser =_userAppService.FindById(output.getUserId());
	 _userAppService.deleteAllUserTokens(foundUser.getUserName());  
		
    }
	
	// ------------ Update userpermission ------------
	@PreAuthorize("hasAnyAuthority('USERPERMISSIONENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateUserpermissionOutput> Update(@PathVariable String id, @RequestBody @Valid UpdateUserpermissionInput userpermission) {
	UserpermissionId userpermissionId =_userpermissionAppService.parseUserpermissionKey(id);
	if(userpermissionId == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	FindUserpermissionByIdOutput currentUserpermission = _userpermissionAppService.FindById(userpermissionId);
		
    if (currentUserpermission == null) {
	   logHelper.getLogger().error("Unable to update. Userpermission with id {} not found.", id);
	   return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
	}
		
    FindUserByIdOutput foundUser =_userAppService.FindById(currentUserpermission.getUserId());
	_userAppService.deleteAllUserTokens(foundUser.getUserName());  
		
		
	return new ResponseEntity(_userpermissionAppService.Update(userpermissionId,userpermission), HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('USERPERMISSIONENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindUserpermissionByIdOutput> FindById(@PathVariable String id) {
	UserpermissionId userpermissionId =_userpermissionAppService.parseUserpermissionKey(id);
	if(userpermissionId == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	FindUserpermissionByIdOutput output = _userpermissionAppService.FindById(userpermissionId);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('USERPERMISSIONENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity Find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }
//		if (sort.isUnsorted()) { sort = new Sort(Sort.Direction.fromString(env.getProperty("fastCode.sort.direction.default")), new String[]{env.getProperty("fastCode.sort.property.default")}); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		return ResponseEntity.ok(_userpermissionAppService.Find(searchCriteria,Pageable));
	}

    @PreAuthorize("hasAnyAuthority('USERPERMISSIONENTITY_READ')")
	@RequestMapping(value = "/{userpermissionid}/user", method = RequestMethod.GET)
	public ResponseEntity<GetUserOutput> GetUser(@PathVariable String id) {
	UserpermissionId userpermissionId =_userpermissionAppService.parseUserpermissionKey(id);
	if(userpermissionId == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	GetUserOutput output= _userpermissionAppService.GetUser(userpermissionId);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(output, HttpStatus.OK);
	}
  
    @PreAuthorize("hasAnyAuthority('USERPERMISSIONENTITY_READ')")
	@RequestMapping(value = "/{userpermissionid}/permission", method = RequestMethod.GET)
	public ResponseEntity<GetPermissionOutput> GetPermission(@PathVariable String id) {
	UserpermissionId userpermissionId =_userpermissionAppService.parseUserpermissionKey(id);
	if(userpermissionId == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	GetPermissionOutput output= _userpermissionAppService.GetPermission(userpermissionId);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(output, HttpStatus.OK);
	}
  

}

