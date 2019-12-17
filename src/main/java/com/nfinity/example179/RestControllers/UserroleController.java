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
import com.nfinity.example179.domain.model.UserroleId;
import com.nfinity.example179.CommonModule.Search.SearchCriteria;
import com.nfinity.example179.CommonModule.Search.SearchUtils;
import com.nfinity.example179.CommonModule.application.OffsetBasedPageRequest;
import com.nfinity.example179.CommonModule.domain.EmptyJsonResponse;
import com.nfinity.example179.application.Authorization.Userrole.UserroleAppService;
import com.nfinity.example179.application.Authorization.Userrole.Dto.*;
import com.nfinity.example179.application.Authorization.Userrole.Dto.GetRoleOutput;
import com.nfinity.example179.application.Authorization.User.UserAppService;
import com.nfinity.example179.application.Authorization.User.Dto.*;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;

@RestController
@RequestMapping("/userrole")
public class UserroleController {

	@Autowired
	private UserroleAppService _userroleAppService;
    
    @Autowired
	private UserAppService  _userAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;

    @PreAuthorize("hasAnyAuthority('USERROLEENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateUserroleOutput> Create(@RequestBody @Valid CreateUserroleInput userrole) {
	CreateUserroleOutput output=_userroleAppService.Create(userrole);
		
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

	// ------------ Delete userrole ------------
	@PreAuthorize("hasAnyAuthority('USERROLEENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void Delete(@PathVariable String id) {
	UserroleId userroleid =_userroleAppService.parseUserroleKey(id);
	if(userroleid == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	FindUserroleByIdOutput output = _userroleAppService.FindById(userroleid);
	if (output == null) {
		logHelper.getLogger().error("There does not exist a userrole with a id=%s", id);
		throw new EntityNotFoundException(
			String.format("There does not exist a userrole with a id=%s", id));
	}
	 _userroleAppService.Delete(userroleid);
	 
	  FindUserByIdOutput foundUser =_userAppService.FindById(output.getUserId());
	 _userAppService.deleteAllUserTokens(foundUser.getUserName());  
		
    }
	
	// ------------ Update userrole ------------
	@PreAuthorize("hasAnyAuthority('USERROLEENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateUserroleOutput> Update(@PathVariable String id, @RequestBody @Valid UpdateUserroleInput userrole) {
	UserroleId userroleid =_userroleAppService.parseUserroleKey(id);
	if(userroleid == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
			String.format("Invalid id=%s", id));
	}
	FindUserroleByIdOutput currentUserrole = _userroleAppService.FindById(userroleid);
			
	if (currentUserrole == null) {
		logHelper.getLogger().error("Unable to update. Userrole with id {} not found.", id);
		return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
	}
	
	FindUserByIdOutput foundUser =_userAppService.FindById(currentUserrole.getUserId());
	_userAppService.deleteAllUserTokens(foundUser.getUserName());  
		
	return new ResponseEntity(_userroleAppService.Update(userroleid,userrole), HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('USERROLEENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindUserroleByIdOutput> FindById(@PathVariable String id) {
	UserroleId userroleid =_userroleAppService.parseUserroleKey(id);
	if(userroleid == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	
	FindUserroleByIdOutput output = _userroleAppService.FindById(userroleid);
	if (output == null) {
		return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
	}
		
	return new ResponseEntity(output, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('USERROLEENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity Find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
	if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
	if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

	Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
	SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
	return ResponseEntity.ok(_userroleAppService.Find(searchCriteria,Pageable));
	}
	
	@PreAuthorize("hasAnyAuthority('USERROLEENTITY_READ')")
	@RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
	public ResponseEntity<GetUserOutput> GetUser(@PathVariable String id) {
	UserroleId userroleid =_userroleAppService.parseUserroleKey(id);
	if(userroleid == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	GetUserOutput output= _userroleAppService.GetUser(userroleid);
	if (output == null) {
		return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
	}
		return new ResponseEntity(output, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyAuthority('USERROLEENTITY_READ')")
	@RequestMapping(value = "/{id}/role", method = RequestMethod.GET)
	public ResponseEntity<GetRoleOutput> GetRole(@PathVariable String id) {
	UserroleId userroleid =_userroleAppService.parseUserroleKey(id);
	if(userroleid == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	
	GetRoleOutput output= _userroleAppService.GetRole(userroleid);
	if (output == null) {
		return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
	}
		return new ResponseEntity(output, HttpStatus.OK);
	}

}

