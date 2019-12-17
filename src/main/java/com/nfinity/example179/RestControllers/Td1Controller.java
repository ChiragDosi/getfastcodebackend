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
import com.nfinity.example179.CommonModule.Search.SearchCriteria;
import com.nfinity.example179.CommonModule.Search.SearchUtils;
import com.nfinity.example179.CommonModule.application.OffsetBasedPageRequest;
import com.nfinity.example179.CommonModule.domain.EmptyJsonResponse;
import com.nfinity.example179.application.Td1.Td1AppService;
import com.nfinity.example179.application.Td1.Dto.*;
import java.util.List;
import java.util.Map;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;

@RestController
@RequestMapping("/td1")
public class Td1Controller {

	@Autowired
	private Td1AppService _td1AppService;

	@Autowired
	private LoggingHelper logHelper;

	@Autowired
	private Environment env;

    @PreAuthorize("hasAnyAuthority('TD1ENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<CreateTd1Output> Create(@RequestBody @Valid CreateTd1Input td1) {
		CreateTd1Output output=_td1AppService.Create(td1);
		
		
		return new ResponseEntity(output, HttpStatus.OK);
	}

	// ------------ Delete td1 ------------
	@PreAuthorize("hasAnyAuthority('TD1ENTITY_DELETE')")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void Delete(@PathVariable String id) {
    FindTd1ByIdOutput output = _td1AppService.FindById(Long.valueOf(id));
	if (output == null) {
		logHelper.getLogger().error("There does not exist a td1 with a id=%s", id);
		throw new EntityNotFoundException(
			String.format("There does not exist a td1 with a id=%s", id));
	}
    _td1AppService.Delete(Long.valueOf(id));
    }
	
	// ------------ Update td1 ------------
    @PreAuthorize("hasAnyAuthority('TD1ENTITY_UPDATE')")
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UpdateTd1Output> Update(@PathVariable String id, @RequestBody @Valid UpdateTd1Input td1) {
	    FindTd1ByIdOutput currentTd1 = _td1AppService.FindById(Long.valueOf(id));
			
		if (currentTd1 == null) {
			logHelper.getLogger().error("Unable to update. Td1 with id {} not found.", id);
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
	    return new ResponseEntity(_td1AppService.Update(Long.valueOf(id),td1), HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('TD1ENTITY_READ')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<FindTd1ByIdOutput> FindById(@PathVariable String id) {
    FindTd1ByIdOutput output = _td1AppService.FindById(Long.valueOf(id));
		if (output == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity(output, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('TD1ENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity Find(@RequestParam(value="search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }

		Pageable Pageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		
		return ResponseEntity.ok(_td1AppService.Find(searchCriteria,Pageable));
	}


}

