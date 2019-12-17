package com.nfinity.example179.Scheduler.RestControllers;

import com.nfinity.example179.Scheduler.SchedulerService.SchedulerService;
import com.nfinity.example179.CommonModule.Search.SearchCriteria;
import com.nfinity.example179.CommonModule.Search.SearchUtils;
import com.nfinity.example179.CommonModule.application.OffsetBasedPageRequest;
import com.nfinity.example179.CommonModule.domain.EmptyJsonResponse;
import com.nfinity.example179.Scheduler.application.Dto.GetJobOutput;
import com.nfinity.example179.Scheduler.application.Dto.GetTriggerOutput;
import com.nfinity.example179.Scheduler.application.Dto.TriggerCreationDetails;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/triggers")
public class TriggerController {

	@Autowired
	SchedulerService schedulerService;

	@Autowired
	private Environment env;

    @PreAuthorize("hasAnyAuthority('TRIGGERDETAILSENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<TriggerCreationDetails> CreateTrigger(@RequestBody @Valid TriggerCreationDetails obj) throws Exception {
		if (obj.getJobName() == null || obj.getJobGroup() == null || obj.getTriggerName() == null || obj.getTriggerGroup() == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.PARTIAL_CONTENT);

		}
		boolean status = schedulerService.CreateTrigger(obj);
		if( status== false) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.CONFLICT);
		}
		return new ResponseEntity(obj, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('TRIGGERDETAILSENTITY_UPDATE')")
	@RequestMapping(value = "/{triggerName}/{triggerGroup}", method = RequestMethod.PUT)
	public ResponseEntity<String> UpdateTrigger(@PathVariable String triggerName, @PathVariable String triggerGroup, @RequestBody @Valid TriggerCreationDetails obj) throws SchedulerException {
		if(triggerName == null || triggerGroup == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}

		obj.setTriggerName(triggerName);
		obj.setTriggerGroup(triggerGroup);
		boolean status = schedulerService.UpdateTrigger(obj);
		if(status == false) {
			throw new EntityNotFoundException(
					String.format("There does not exist a trigger "));
		}
		return new ResponseEntity("Trigger Updation Status " + status, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('TRIGGERDETAILSENTITY_READ')")
	@RequestMapping(value = "/{triggerName}/{triggerGroup}", method = RequestMethod.GET)
	public ResponseEntity<GetTriggerOutput> ReturnTrigger(@PathVariable String triggerName, @PathVariable String triggerGroup) throws SchedulerException {
		if(triggerName == null || triggerGroup == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		GetTriggerOutput output = schedulerService.ReturnTrigger(triggerName, triggerGroup);
		if(output == null) {
			throw new EntityNotFoundException(
					String.format("There does not exist a trigger with a triggerName=%s and triggerGroup=%s", triggerName , triggerGroup));
		}
		return new ResponseEntity(output, HttpStatus.OK);
	}
  
    @PreAuthorize("hasAnyAuthority('TRIGGERDETAILSENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<GetTriggerOutput>> ListAllTriggers( @RequestParam(value = "search", required=false) String search,@RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }
		//if (sort.isUnsorted()) { sort = new Sort(Sort.Direction.fromString(env.getProperty("fastCode.sort.direction.default")), new String[]{env.getProperty("fastCode.sort.property.default")}); }

		Pageable offsetPageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		List<GetTriggerOutput> list = schedulerService.ListAllTriggers(searchCriteria,offsetPageable);

		return new ResponseEntity(list, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('TRIGGERDETAILSENTITY_READ')")
	@RequestMapping(value = "/getTriggerGroups", method = RequestMethod.GET)
	public ResponseEntity<List<String>> ListAllTriggerGroups() throws SchedulerException {
		List<String> list = schedulerService.ListAllTriggerGroups();

		return new ResponseEntity(list, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('TRIGGERDETAILSENTITY_READ')")
	@RequestMapping(value = "/pauseTrigger/{triggerName}/{triggerGroup}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> PauseTrigger(@PathVariable String triggerName, @PathVariable String triggerGroup) throws SchedulerException {
		if(triggerName == null || triggerGroup == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		boolean status = schedulerService.PauseTrigger(triggerName, triggerGroup);
		if(status == false) {
			throw new EntityNotFoundException(
					String.format("There does not exist a trigger with a triggerName=%s and triggerGroup=%s", triggerName , triggerGroup));
		}
		return new ResponseEntity(status, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('TRIGGERDETAILSENTITY_READ')")
	@RequestMapping(value = "/resumeTrigger/{triggerName}/{triggerGroup}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> ResumeTrigger(@PathVariable String triggerName, @PathVariable String triggerGroup) throws SchedulerException {
		if(triggerName == null || triggerGroup == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		boolean status = schedulerService.ResumeTrigger(triggerName, triggerGroup);
		if(status == false) {
			throw new EntityNotFoundException(
					String.format("There does not exist a trigger with a triggerName=%s and triggerGroup=%s", triggerName , triggerGroup));
		}
		return new ResponseEntity(status, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('TRIGGERDETAILSENTITY_DELETE')")
	@RequestMapping(value = "/{triggerName}/{triggerGroup}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> CancelTrigger(@PathVariable String triggerName, @PathVariable String triggerGroup) throws SchedulerException {
		if(triggerName == null || triggerGroup == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		boolean status = schedulerService.CancelTrigger(triggerName, triggerGroup);
		if(status == false) {
			throw new EntityNotFoundException(
					String.format("There does not exist a trigger with a triggerName=%s and triggerGroup=%s", triggerName , triggerGroup));
		}
		return new ResponseEntity(status, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('TRIGGERDETAILSENTITY_READ')")
	@RequestMapping(value = "/{triggerName}/{triggerGroup}/jobExecutionHistory", method = RequestMethod.GET)
	public ResponseEntity<List<GetJobOutput>> ExecutionHistoryByTrigger(@PathVariable String triggerName, @PathVariable String triggerGroup) {
		if(triggerName == null || triggerGroup == null) {
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(schedulerService.ExecutionHistoryByTrigger(triggerName, triggerGroup), HttpStatus.OK);
	}
}
