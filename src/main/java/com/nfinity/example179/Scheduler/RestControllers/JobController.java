package com.nfinity.example179.Scheduler.RestControllers;

import com.nfinity.example179.Scheduler.SchedulerService.SchedulerService;
import com.nfinity.example179.CommonModule.Search.SearchCriteria;
import com.nfinity.example179.CommonModule.Search.SearchUtils;
import com.nfinity.example179.CommonModule.application.OffsetBasedPageRequest;
import com.nfinity.example179.CommonModule.domain.EmptyJsonResponse;
import com.nfinity.example179.Scheduler.application.Dto.GetJobOutput;
import com.nfinity.example179.Scheduler.application.Dto.JobDetails;
import com.nfinity.example179.Scheduler.application.Dto.JobListOutput;
import com.nfinity.example179.Scheduler.application.Dto.TriggerDetails;

import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/jobs")
public class JobController {

	@Autowired
	SchedulerService schedulerService;

	@Autowired
	private Environment env;

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_READ')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<JobListOutput>> ListAllJobs(@RequestParam(value = "search", required=false) String search, @RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception {
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }
		//if (sort.isUnsorted()) { sort = new Sort(Sort.Direction.fromString(env.getProperty("fastCode.sort.direction.default")), new String[]{env.getProperty("fastCode.sort.property.default")}); }

		Pageable offsetPageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		List<JobListOutput> list = schedulerService.ListAllJobs(searchCriteria,offsetPageable);

		return new ResponseEntity(list, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_READ')")
	@RequestMapping(value = "/getJobGroups", method = RequestMethod.GET)
	public ResponseEntity<List<String>> ListAllJobGroups() throws SchedulerException, IOException {
		List<String> list = schedulerService.ListAllJobGroups();

		return new ResponseEntity(list, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_READ')")
	@RequestMapping(value = "/{jobName}/{jobGroup}", method = RequestMethod.GET)
	public ResponseEntity<JobDetails> ReturnJob(@PathVariable String jobName, @PathVariable String jobGroup) throws SchedulerException {

		if(jobName == null || jobGroup == null)
		{
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		JobDetails detail=schedulerService.ReturnJob(jobName, jobGroup);
        if(detail == null)
        {
        	throw new EntityNotFoundException(
					String.format("There does not exist a job with a jobName=%s and jobGroup=%s", jobName , jobGroup));
        }
		
		return new ResponseEntity(detail, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_READ')")
	@RequestMapping(value = "/getJobClasses", method = RequestMethod.GET)
	public ResponseEntity<List<String>> ListAllJobClasses() throws SchedulerException {
		List<String> list = schedulerService.ListAllJobClasses();

		return new ResponseEntity(list, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_READ')")
	@RequestMapping(value = "/executingJobs", method = RequestMethod.GET)
	public ResponseEntity<JobDetails> ListCurrentlyExecutingJobs() throws SchedulerException {
		List<JobDetails> list = schedulerService.CurrentlyExecutingJobs();
       
		return new ResponseEntity(list, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_DELETE')")
	@RequestMapping(value = "/{jobName}/{jobGroup}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> DeleteJob(@PathVariable String jobName, @PathVariable String jobGroup) throws SchedulerException {

		if(jobName == null || jobGroup == null)
		{
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		boolean status = schedulerService.DeleteJob(jobName, jobGroup);
		if(status==false)
		{
		throw new EntityNotFoundException(
				String.format("There does not exist a job with a jobName=%s and jobGroup=%s", jobName , jobGroup));
		}
		return new ResponseEntity(status, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_UPDATE')")
	@RequestMapping(value = "/{jobName}/{jobGroup}", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> UpdateJob(@PathVariable String jobName, @PathVariable String jobGroup, @RequestBody @Valid JobDetails obj) throws SchedulerException {

		if(jobName == null || jobGroup == null)
		{
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		obj.setJobName(jobName);
		obj.setJobGroup(jobGroup);
		boolean status = schedulerService.UpdateJob(obj);
		if(status==false)
		{
		throw new EntityNotFoundException(
				String.format("There does not exist a job with a jobName=%s and jobGroup=%s", jobName , jobGroup));
		}
		return new ResponseEntity(status, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_READ')")
	@RequestMapping(value = "/pauseJob/{jobName}/{jobGroup}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> PauseJob(@PathVariable String jobName, @PathVariable String jobGroup) throws SchedulerException {
		if(jobName == null || jobGroup == null)
		{
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		boolean status = schedulerService.PauseJob(jobName, jobGroup);
		if(status==false)
		{
		throw new EntityNotFoundException(
				String.format("There does not exist a job with a jobName=%s and jobGroup=%s", jobName , jobGroup));
		}
		return new ResponseEntity(status, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_READ')")
	@RequestMapping(value = "/resumeJob/{jobName}/{jobGroup}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> ResumeJob(@PathVariable String jobName, @PathVariable String jobGroup) throws SchedulerException {
		if(jobName == null || jobGroup == null)
		{
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		boolean status = schedulerService.ResumeJob(jobName, jobGroup);
		if(status==false)
		{
		throw new EntityNotFoundException(
				String.format("There does not exist a job with a jobName=%s and jobGroup=%s", jobName , jobGroup));
		}
		return new ResponseEntity(status, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_CREATE')")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<JobDetails> CreateJob(@RequestBody @Valid JobDetails obj) throws SchedulerException, ClassNotFoundException {
		if(obj.getJobClass() == null || obj.getJobName() == null || obj.getJobGroup() == null)
		{
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.PARTIAL_CONTENT);
		}
		boolean status = schedulerService.CreateJob(obj);
		if(status==false)
		{
			throw new EntityExistsException(
					String.format("There already exists a job with a jobName=%s and jobGroup=%s", obj.getJobName() ,obj.getJobGroup()));
    }
		
		return new ResponseEntity(obj, HttpStatus.OK);
	}

    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_READ')")
	@RequestMapping(value = "/{jobName}/{jobGroup}/triggers", method = RequestMethod.GET)
	public ResponseEntity<List<TriggerDetails>> ReturnTriggerForJob(@PathVariable String jobName, @PathVariable String jobGroup) throws SchedulerException {
		if(jobName == null || jobGroup == null)
		{
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		
		List<TriggerDetails> triggerDetails = schedulerService.ReturnTriggersForAJob(new JobKey(jobName, jobGroup));
		if(triggerDetails==null)
		{
			throw new EntityNotFoundException(
					String.format("There does not exist a job with a jobName=%s and jobGroup=%s", jobName , jobGroup));
		}
		return new ResponseEntity(triggerDetails, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_READ')")
	@RequestMapping(value = "/{jobName}/{jobGroup}/jobExecutionHistory", method = RequestMethod.GET)
	public ResponseEntity<List<GetJobOutput>> ExecutionHistoryByJob(@PathVariable String jobName, @PathVariable String jobGroup) {
		if(jobName == null || jobGroup == null)
		{
			return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.NOT_FOUND);
		}
		List<GetJobOutput> list = schedulerService.ExecutionHistoryByJob(jobName, jobGroup);
		return new ResponseEntity(list, HttpStatus.OK);
	}
    
    @PreAuthorize("hasAnyAuthority('JOBDETAILSENTITY_READ')")
	@RequestMapping(value= "/jobExecutionHistory", method = RequestMethod.GET)
	public ResponseEntity<List<GetJobOutput>> ExecutionHistory (@RequestParam(value = "search", required=false) String search,@RequestParam(value = "offset", required=false) String offset, @RequestParam(value = "limit", required=false) String limit, Sort sort) throws Exception
	{
		if (offset == null) { offset = env.getProperty("fastCode.offset.default"); }
		if (limit == null) { limit = env.getProperty("fastCode.limit.default"); }
		//if (sort.isUnsorted()) { sort = new Sort(Sort.Direction.fromString(env.getProperty("fastCode.sort.direction.default")), new String[]{env.getProperty("fastCode.sort.property.default")}); }

		Pageable offsetPageable = new OffsetBasedPageRequest(Integer.parseInt(offset), Integer.parseInt(limit), sort);
		SearchCriteria searchCriteria = SearchUtils.generateSearchCriteriaObject(search);
		return new ResponseEntity(schedulerService.ExecutionHistory(searchCriteria,offsetPageable),HttpStatus.OK);

	}


}
