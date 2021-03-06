package com.nfinity.example179.RestControllers;

import com.nfinity.example179.application.Authorization.Userpermission.UserpermissionAppService;
import com.nfinity.example179.application.Authorization.Userrole.UserroleAppService;
import com.nfinity.example179.application.Authorization.Userrole.Dto.FindUserroleByIdOutput;
import com.nfinity.example179.application.Authorization.Userpermission.Dto.FindUserpermissionByIdOutput;
import com.nfinity.example179.application.Authorization.User.UserAppService;
import com.nfinity.example179.application.Authorization.User.Dto.*;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserAppService _userAppService;
    
    @Autowired
	private UserpermissionAppService  _userpermissionAppService;
	
	@Autowired
	private UserroleAppService  _userroleAppService;
	
	@Autowired
    private PasswordEncoder pEncoder;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;

	// CRUD Operations
	// ------------ Create a user ------------
	@PreAuthorize("hasAnyAuthority('USERENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateUserOutput> Create(@RequestBody @Valid CreateUserInput user) {
		 FindUserByNameOutput foundUser = _userAppService.FindByUserName(user.getUserName());

	     if (foundUser != null) {
	     	logHelper.getLogger().error("There already exists a user with a name=%s", user.getUserName());
	        throw new EntityExistsException(
	        	String.format("There already exists a user with email address=%s", user.getUserName()));
	    }
	        
	    user.setPassword(pEncoder.encode(user.getPassword()));
	    CreateUserOutput output=_userAppService.Create(user);
		if(output==null)
		{
			logHelper.getLogger().error("No record found");
		throw new EntityNotFoundException(
			String.format("No record found"));
	    }
		
		return new ResponseEntity(output, HttpStatus.OK);
	}

	// ------------ Delete a user ------------
	@PreAuthorize("hasAnyAuthority('USERENTITY_DELETE')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void Delete(@PathVariable String id) {
    	FindUserByIdOutput existing = _userAppService.FindById(Long.valueOf(id));

        if (existing == null) {
        	logHelper.getLogger().error("There does not exist a user with a id=%s", id);
        	throw new EntityNotFoundException(
	        	String.format("There does not exist a user with a id=%s", id));
	     
        }
    	
		_userAppService.Delete(Long.valueOf(id));
	}
	
	// ------------ Update user ------------
	@PreAuthorize("hasAnyAuthority('USERENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateUserOutput> Update(@PathVariable String id, @RequestBody @Valid UpdateUserInput user) {
    	FindUserWithAllFieldsByIdOutput currentUser = _userAppService.FindWithAllFieldsById(Long.valueOf(id));
		
		if (currentUser == null) {
			logHelper.getLogger().error("Unable to update. User with id {} not found.", id);
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		user.setPassword(currentUser.getPassword());
		if(currentUser.getIsActive() && !user.getIsActive()) { 
 
            _userAppService.deleteAllUserTokens(currentUser.getUserName()); 
        } 
    return new ResponseEntity(_userAppService.Update(Long.valueOf(id),user), HttpStatus.OK);
	}

	// ------------ Retrieve a user ------------
	@PreAuthorize("hasAnyAuthority('USERENTITY_READ')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindUserByIdOutput> FindById(@PathVariable String id) {
    FindUserByIdOutput output = _userAppService.FindById(Long.valueOf(id));
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('USERENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity Find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit));
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		return ResponseEntity.ok(_userAppService.Find(searchCriteria,Pageable));
	}
   
    @PreAuthorize("hasAnyAuthority('USERENTITY_READ')")
	@RequestMapping(value = "/{userid}/userpermission", method = RequestMethod.GET)
	public ResponseEntity GetUserpermission(@PathVariable String userid, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit));
		
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_userAppService.parseUserpermissionJoinColumn(userid);
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
 
    @PreAuthorize("hasAnyAuthority('USERENTITY_READ')")
	@RequestMapping(value = "/{id}/userrole", method = RequestMethod.GET)
	public ResponseEntity GetUserrole(@PathVariable String id, @RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit)throws Exception {
   		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }
		
		Pageable pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit));
		
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		Map<String,String> joinColDetails=_userAppService.parseUserroleJoinColumn(id);
		if(joinColDetails== null)
		{
			logHelper.getLogger().error("Invalid Join Column");
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		searchCriteria.setJoinColumns(joinColDetails);
		
    	List<FindUserroleByIdOutput> output = _userroleAppService.Find(searchCriteria,pageable);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}   


}