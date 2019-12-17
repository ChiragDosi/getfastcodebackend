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
import com.nfinity.example179.domain.model.RolepermissionId;
import com.nfinity.example179.CommonModule.Search.SearchCriteria;
import com.nfinity.example179.CommonModule.Search.SearchUtils;
import com.nfinity.example179.CommonModule.application.OffsetBasedPageRequest;
import com.nfinity.example179.CommonModule.domain.EmptyJsonResponse;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;
import com.nfinity.example179.application.Authorization.Rolepermission.RolepermissionAppService;
import com.nfinity.example179.application.Authorization.Rolepermission.Dto.*;

@RestController
@RequestMapping("/rolepermission")
public class RolepermissionController {

	@Autowired
	private RolepermissionAppService _rolepermissionAppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;
    
    @PreAuthorize("hasAnyAuthority('ROLEPERMISSIONENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateRolepermissionOutput> Create(@RequestBody @Valid CreateRolepermissionInput rolepermission) {
		CreateRolepermissionOutput output=_rolepermissionAppService.Create(rolepermission);
		if(output==null)
		{
			logHelper.getLogger().error("No record found");
		throw new EntityNotFoundException(
				String.format("No record found"));
	    }
		
		return new ResponseEntity(output, HttpStatus.OK);
	}

	// ------------ Delete rolepermission ------------
	@PreAuthorize("hasAnyAuthority('ROLEPERMISSIONENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void Delete(@PathVariable String id) {
	RolepermissionId rolepermissionId =_rolepermissionAppService.parseRolepermissionKey(id);
	if(rolepermissionId == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	FindRolepermissionByIdOutput output = _rolepermissionAppService.FindById(rolepermissionId);
	if (output == null) {
		logHelper.getLogger().error("There does not exist a rolepermission with a id=%s", id);
		throw new EntityNotFoundException(
			String.format("There does not exist a rolepermission with a id=%s", id));
	}
	 _rolepermissionAppService.Delete(rolepermissionId);
    }
	
	// ------------ Update rolepermission ------------
	@PreAuthorize("hasAnyAuthority('ROLEPERMISSIONENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateRolepermissionOutput> Update(@PathVariable String id, @RequestBody @Valid UpdateRolepermissionInput rolepermission) {
	RolepermissionId rolepermissionId =_rolepermissionAppService.parseRolepermissionKey(id);
	if(rolepermissionId == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	FindRolepermissionByIdOutput currentRolepermission = _rolepermissionAppService.FindById(rolepermissionId);
		
		if (currentRolepermission == null) {
			logHelper.getLogger().error("Unable to update. Rolepermission with id {} not found.", id);
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
	return new ResponseEntity(_rolepermissionAppService.Update(rolepermissionId,rolepermission), HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('ROLEPERMISSIONENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindRolepermissionByIdOutput> FindById(@PathVariable String id) {
	RolepermissionId rolepermissionId =_rolepermissionAppService.parseRolepermissionKey(id);
	if(rolepermissionId == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	FindRolepermissionByIdOutput output = _rolepermissionAppService.FindById(rolepermissionId);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('ROLEPERMISSIONENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity Find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }
//		if (sort.isUnsorted()) { sort = new Sort(Sort.Direction.fromString(env.getProperty("fastCode.sort.direction.default")), new String[]{env.getProperty("fastCode.sort.property.default")}); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		return ResponseEntity.ok(_rolepermissionAppService.Find(searchCriteria,Pageable));
	}

    @PreAuthorize("hasAnyAuthority('ROLEPERMISSIONENTITY_READ')")
	@RequestMapping(value = "/{rolepermissionid}/permission", method = RequestMethod.GET)
	public ResponseEntity<GetPermissionOutput> GetPermission(@PathVariable String id) {
	RolepermissionId rolepermissionId =_rolepermissionAppService.parseRolepermissionKey(id);
	if(rolepermissionId == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	GetPermissionOutput output= _rolepermissionAppService.GetPermission(rolepermissionId);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(output, HttpStatus.OK);
	}
  
    @PreAuthorize("hasAnyAuthority('ROLEPERMISSIONENTITY_READ')")
	@RequestMapping(value = "/{rolepermissionid}/role", method = RequestMethod.GET)
	public ResponseEntity<GetRoleOutput> GetRole(@PathVariable String id) {
	RolepermissionId rolepermissionId =_rolepermissionAppService.parseRolepermissionKey(id);
	if(rolepermissionId == null)
	{
		logHelper.getLogger().error("Invalid id=%s", id);
		throw new EntityNotFoundException(
				String.format("Invalid id=%s", id));
	}
	GetRoleOutput output= _rolepermissionAppService.GetRole(rolepermissionId);
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(output, HttpStatus.OK);
	}
  

}

