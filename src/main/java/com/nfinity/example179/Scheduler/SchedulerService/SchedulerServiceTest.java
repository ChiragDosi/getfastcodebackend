package com.nfinity.example179.Scheduler.SchedulerService;

import static org.mockito.ArgumentMatchers.anyString;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nfinity.example179.Scheduler.Constants.QuartzConstants;
import com.nfinity.example179.CommonModule.Search.SearchCriteria;
import com.nfinity.example179.CommonModule.Search.SearchFields;
import com.nfinity.example179.Scheduler.application.Dto.GetJobOutput;
import com.nfinity.example179.Scheduler.application.Dto.GetTriggerOutput;
import com.nfinity.example179.Scheduler.application.Dto.JobDetails;
import com.nfinity.example179.Scheduler.application.Dto.TriggerCreationDetails;
import com.nfinity.example179.Scheduler.application.Dto.TriggerDetails;
import com.nfinity.example179.Scheduler.domain.model.QJobDetailsEntity;
import com.nfinity.example179.Scheduler.domain.model.QJobHistoryEntity;
import com.nfinity.example179.Scheduler.domain.model.QTriggerDetailsEntity;
import com.nfinity.example179.Scheduler.domain.model.JobDetailsEntity;
import com.nfinity.example179.Scheduler.domain.JobDetailsManager;
import com.nfinity.example179.Scheduler.domain.model.JobHistoryEntity;
import com.nfinity.example179.Scheduler.domain.JobHistoryManager;
import com.nfinity.example179.Scheduler.domain.model.TriggerDetailsEntity;
import com.nfinity.example179.Scheduler.domain.TriggerDetailsManager;
import com.nfinity.example179.Scheduler.jobs.sampleJob;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class SchedulerServiceTest {

	@InjectMocks
	SchedulerService schedulerService = new SchedulerService();

	@Mock
	private SchedulerServiceUtil schedulerServiceUtil;
	
	@Mock
	TriggerDetailsManager triggerDetailsManager;
	
	@Mock
	JobDetailsManager jobDetailsManager;
	
	@Mock
	JobHistoryManager jobHistoryManager;

	@Mock
	SchedulerFactoryBean schedulerFactoryBean;

	@Mock
	private Scheduler scheduler;
	
	@Mock
	private JobExecutionContext executionContext;
	
	@Mock
	private JobDetail sch_jobDetail;
	
	@Mock
	private Trigger trigger;
	
	private QuartzConstants quartzConstant;

	@Mock
	private Logger loggerMock;

	@Mock
	private LoggingHelper logHelper;

	//private static long ID=15;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(schedulerService);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
		Mockito.when(schedulerService.getScheduler()).thenReturn(scheduler);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void ListAllJobs_JobExists_RetrunList() throws Exception
	{
		List<JobDetailsEntity> list = new ArrayList<>();
		JobDetailsEntity jobDetails=mock ( JobDetailsEntity.class);
		list.add(jobDetails);
		Page<JobDetailsEntity > foundPage = new PageImpl(list);
		Pageable pageable =mock(Pageable.class);
		SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");

		Mockito.when(jobDetailsManager.FindAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Mockito.when(schedulerServiceUtil.isJobRunning(anyString(), anyString())).thenReturn(false);
		Mockito.when(schedulerServiceUtil.getJobState(anyString(), anyString())).thenReturn("NONE");
		Assertions.assertThat(schedulerService.ListAllJobs(search,pageable)).isNotNull();

	}
	
	@Test
	public void  searchAllPropertiesForJobDetail_SearchIsNotNull_ReturnBooleanBuilder()
	{
		String search= "xyz";
		String operator= "equals";
		QJobDetailsEntity jobs = QJobDetailsEntity.jobDetailsEntity;
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(jobs.jobName.eq(search));
		builder.or(jobs.jobGroup.eq(search));
		builder.or(jobs.jobClassName.eq(search));

		Assertions.assertThat(schedulerService.searchAllPropertiesForJobDetails(jobs,search,operator)).isEqualTo(builder);
	}

	@Test
	public void searchSpecificPropertyForJobDetail_PropertyExists_ReturnBooleanBuilder() throws Exception
	{
		String operator= "equals";
		List<String> list = new ArrayList<>();
		list.add("jobName");
		list.add("jobGroup");
		QJobDetailsEntity jobs = QJobDetailsEntity.jobDetailsEntity;
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(jobs.jobName.eq("xyz"));	
		builder.or(jobs.jobGroup.eq("xyz"));

		Assertions.assertThat(schedulerService.searchSpecificPropertyForJobDetails(jobs,list,"xyz",operator)).isEqualTo(builder);
	}
	
	@Test
	public void searchKeyValuePairForJobDetail_PropertyExists_ReturnBooleanBuilder()
	{
		QJobDetailsEntity jobs = QJobDetailsEntity.jobDetailsEntity;
		SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
		Map<String,SearchFields> map = new HashMap();
		map.put("jobName", searchFields);
	
        BooleanBuilder builder = new BooleanBuilder();
		builder.and(jobs.jobName.eq("xyz"));
        
        Assertions.assertThat(schedulerService.searchKeyValuePairForJobDetails(jobs, map)).isEqualTo(builder);
	}

	@Test(expected = Exception.class)
	public void checkPropertiesForJobDetail_PropertyDoesNotExist_ThrowException() throws Exception
	{
		List<String> list = new ArrayList<>();
		list.add("test");

		schedulerService.checkPropertiesForJobDetails(list);
	}
	@Test
	public void checkPropertiesForJobDetail_PropertyExists_ReturnNothing() throws Exception
	{
		List<String> list = new ArrayList<>();
		list.add("jobName");

		schedulerService.checkPropertiesForJobDetails(list);
	}
	
	@Test
	public void searchJobDetail_SearchIsNotNullAndSearchContainsCaseOne_ReturnBooleanBuilder() throws Exception
	{
		QJobDetailsEntity jobs = QJobDetailsEntity.jobDetailsEntity;
		SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");
        BooleanBuilder builder = new BooleanBuilder();
		builder.or(jobs.jobName.eq("xyz"));
		builder.or(jobs.jobGroup.eq("xyz"));
		builder.or(jobs.jobClassName.eq("xyz"));
        
        Assertions.assertThat(schedulerService.SearchJobDetails(search)).isEqualTo(builder);
        
	}
	
	@Test
	public void searchJobDetail_SearchIsNotNullAndSearchContainsCaseTwo_ReturnBooleanBuilder() throws Exception
	{
		QJobDetailsEntity jobs = QJobDetailsEntity.jobDetailsEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(2);
		search.setValue("xyz");
		search.setOperator("equals");
		fields.setFieldName("jobName");
        fieldsList.add(fields);
        search.setFields(fieldsList);
    	BooleanBuilder builder = new BooleanBuilder();
    	builder.or(jobs.jobName.eq("xyz"));
		
        Assertions.assertThat(schedulerService.SearchJobDetails(search)).isEqualTo(builder);
        
	}
	@Test
	public void searchJobDetail_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception
	{
		Map<String,SearchFields> map = new HashMap<>();
		QJobDetailsEntity jobs = QJobDetailsEntity.jobDetailsEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		fields.setFieldName("jobName");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
    	BooleanBuilder builder = new BooleanBuilder();
    	builder.and(jobs.jobName.eq("xyz"));
        
        Assertions.assertThat(schedulerService.SearchJobDetails(search)).isEqualTo(builder);
        
	}
	
	@Test
	public void searchJobDetail_StringIsNull_ReturnNull() throws Exception
	{
		 Assertions.assertThat(schedulerService.SearchJobDetails(null)).isEqualTo(null);
	}
	
	
	@Test
	public void TriggerDetailsForJob_JobKeyExists_ReturnTriggerDeatils() throws SchedulerException
	{
		List<TriggerDetails> triggerDetails = new ArrayList<TriggerDetails>();
		JobKey jobKey = new JobKey("job1","group1");

		Trigger trigger = mock(Trigger.class);
		TriggerDetails triggerDetail = new TriggerDetails();

		Mockito.when(scheduler.checkExists(Mockito.any(JobKey.class))).thenReturn(true);
		Mockito.when(schedulerServiceUtil.ReturnTriggerDetails(Mockito.any(Trigger.class))).thenReturn(triggerDetail);

		Assertions.assertThat(schedulerService.ReturnTriggersForAJob(jobKey)).isEqualTo(triggerDetails);
	}
	
	@Test
	public void TriggerDetailsForJob_JobKeyDoesNotExist_ReturnTriggerDeatils() throws SchedulerException
	{
		JobKey jobKey = new JobKey("job1","group1");

		Mockito.when(scheduler.checkExists(Mockito.any(JobKey.class))).thenReturn(false);
		Assertions.assertThat(schedulerService.ReturnTriggersForAJob(jobKey)).isEqualTo(null);
	}

	@Test
	public void CurrentExecutingJobs_JobKeyIsNotNull_ReturnJobs() throws SchedulerException
	{

		List<JobDetails> jobDetails = new ArrayList<JobDetails>();
		List<JobExecutionContext> executingJobs = new ArrayList<>();
        executingJobs.add(executionContext);
        
		List<JobKey> executingJobKeys = new ArrayList<>();
		JobKey jobKey = new JobKey("job1","group1");
		JobDetails jobDetail = new JobDetails();
        jobDetails.add(jobDetail);
        
		Mockito.when(scheduler.getCurrentlyExecutingJobs()).thenReturn(executingJobs);
        Mockito.when(executionContext.getJobDetail()).thenReturn(sch_jobDetail);
        Mockito.when(sch_jobDetail.getKey()).thenReturn(jobKey);
        Mockito.when(schedulerServiceUtil.ReturnJobDetails(Mockito.any(JobKey.class))).thenReturn(jobDetail);
     
        Assertions.assertThat(schedulerService.CurrentlyExecutingJobs()).isEqualTo(jobDetails);
	}
	
	@Test
	public void ListAllJobClasses_JobClassExists_ReturnList() throws ClassNotFoundException
	{
		List<String> classList = new ArrayList<String>();
        classList.add("com.nfinity.fastcode.jobs.Job3");
        classList.add("com.nfinity.fastcode.jobs.Job2");
        classList.add("com.nfinity.fastcode.jobs.Job1");
  	
		Assertions.assertThat(schedulerService.ListAllJobClasses().size()).isEqualTo(classList.size());
	}
	
	@Test
	public void ListAllJobGroups_GetGroupsFromShceduler_ReturnList() throws SchedulerException, IOException
	{
		List<String> groupsList = new ArrayList<String>() ;
		
		Mockito.when(scheduler.getJobGroupNames()).thenReturn(groupsList);
		Assertions.assertThat(schedulerService.ListAllJobGroups()).isEqualTo(groupsList);
	}
	
	@Test
	public void PauseAJob_JobExists_JobPaused() throws SchedulerException
	{
		Mockito.when(scheduler.checkExists(any(JobKey.class))).thenReturn(true);
		Assertions.assertThat(schedulerService.PauseJob("job1", "group1")).isEqualTo(true);
	}
	
	@Test
	public void PauseAJob_JobDoesNotExist_ReturnFalse() throws SchedulerException
	{	
		Mockito.when(scheduler.checkExists(Mockito.any(JobKey.class))).thenReturn(false);
		Assertions.assertThat(schedulerService.PauseJob("job1", "group1")).isEqualTo(false);
	}
	
	@Test
	public void ResumeJob_JobExists_JobResumed() throws SchedulerException
	{	
		Mockito.when(scheduler.checkExists(Mockito.any(JobKey.class))).thenReturn(true);
		Assertions.assertThat(schedulerService.ResumeJob("job1", "group1")).isEqualTo(true);
	}
	
	@Test
	public void ResumeJob_JobDoesNotExist_ReturnFalse() throws SchedulerException
	{	
		Mockito.when(scheduler.checkExists(Mockito.any(JobKey.class))).thenReturn(false);
		Assertions.assertThat(schedulerService.ResumeJob("job1", "group1")).isEqualTo(false);
	}

	@Test
	public void DeleteJob_JobExists_JobDeleted() throws SchedulerException
	{	
		Mockito.when(scheduler.checkExists(any(JobKey.class))).thenReturn(true);
		Assertions.assertThat(schedulerService.DeleteJob("job1", "group1")).isEqualTo(true);
	}
	
	@Test
	public void DeleteJob_JobDoesNotExist_ReturnFalse() throws SchedulerException
	{	
		Mockito.when(scheduler.checkExists(any(JobKey.class))).thenReturn(false);
		Assertions.assertThat(schedulerService.DeleteJob("job1", "group1")).isEqualTo(false);
	}
	
	@Test
	public void ReturnJob_JobExists_ReturnJobDetails() throws SchedulerException
	{
		List<TriggerDetails> triggerDetails = new ArrayList<TriggerDetails>();
		TriggerDetails details = new TriggerDetails();
		JobDetails jobDetails =mock(JobDetails.class);
		Mockito.when(scheduler.checkExists(any(JobKey.class))).thenReturn(true);
		Mockito.when(schedulerServiceUtil.ReturnJobDetails(any(JobKey.class))).thenReturn(jobDetails);
		Mockito.when(schedulerServiceUtil.ReturnTriggerDetails(any(Trigger.class))).thenReturn(details);
		
		triggerDetails.add(details);
		jobDetails.setTriggerDetails(triggerDetails);
		Assertions.assertThat(schedulerService.ReturnJob("job1", "group1")).isEqualTo(jobDetails);		//Mockito.when(scheduler.getTriggersOfJob(jobKey)).thenReturn(triggers);
	}
	
	@Test
	public void UpdateTrigger_TriggerDoesNotExist_ReturnFalse() throws SchedulerException
	{
		Trigger trigger=mock(Trigger.class);
		TriggerBuilder triggerBuilder = mock(TriggerBuilder.class); 
		Map<String, String> map = new HashMap<String, String>();
		TriggerCreationDetails obj=new TriggerCreationDetails();
		obj.setTriggerName("trigger1");
		obj.setTriggerGroup("group1"); 

		Mockito.when(scheduler.checkExists(any(TriggerKey.class))).thenReturn(false);
	    Assertions.assertThat(schedulerService.UpdateTrigger(obj)).isEqualTo(false);
	}
	
	@Test
	public void UpdateTrigger_TriggerExistsEndTimeIsNullAndTriggerTypeIsSimple_TriggerUpdated() throws SchedulerException
	{
		Trigger trigger=mock(Trigger.class);
		TriggerBuilder triggerBuilder = mock(TriggerBuilder.class); 
		Map<String, String> map = new HashMap<String, String>();
		TriggerCreationDetails obj=new TriggerCreationDetails();
		obj.setTriggerName("trigger1");
		obj.setTriggerGroup("group1");
		obj.setTriggerDescription("abc");
		obj.setTriggerType("Simple");
		obj.setTriggerMapData(map);
		obj.setStartTime(new Date());

		Mockito.when(scheduler.checkExists(any(TriggerKey.class))).thenReturn(true);
		Mockito.when(scheduler.getTrigger(any(TriggerKey.class))).thenReturn(trigger);
		Mockito.when(trigger.getTriggerBuilder()).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.startAt(any(Date.class))).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.withDescription(anyString())).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.build()).thenReturn(trigger);
		Mockito.when(schedulerServiceUtil.UpdateSimpleTrigger(any(TriggerCreationDetails.class),any(TriggerKey.class))).thenReturn(true);
		Assertions.assertThat(schedulerService.UpdateTrigger(obj)).isEqualTo(true);
	}
	
	@Test
	public void UpdateTrigger_TriggerExistsEndTimeIsNullAndTriggerTypeIsCron_TriggerUpdated() throws SchedulerException
	{
		Trigger trigger=mock(Trigger.class);
		TriggerBuilder triggerBuilder = mock(TriggerBuilder.class); 
		Map<String, String> map = new HashMap<String, String>();
		TriggerCreationDetails obj=new TriggerCreationDetails();
		obj.setTriggerName("trigger1");
		obj.setTriggerGroup("group1");
		obj.setTriggerDescription("abc");
		obj.setTriggerType("Cron");
		obj.setCronExpression("0/5 * * * * ?");
		obj.setTriggerMapData(map);
		obj.setStartTime(new Date());

		Mockito.when(scheduler.checkExists(any(TriggerKey.class))).thenReturn(true);
		Mockito.when(scheduler.getTrigger(any(TriggerKey.class))).thenReturn(trigger);
		Mockito.when(trigger.getTriggerBuilder()).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.startAt(any(Date.class))).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.withDescription(anyString())).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.build()).thenReturn(trigger);
		
		Mockito.when(schedulerServiceUtil.UpdateCronTrigger(any(TriggerCreationDetails.class),any(TriggerKey.class))).thenReturn(true);
		Assertions.assertThat(schedulerService.UpdateTrigger(obj)).isEqualTo(true);
	}
	
	@Test
	public void UpdateJob_JobExits_JobUpdated() throws SchedulerException
	{
	    JobBuilder jobBuilder = mock(JobBuilder.class);
		Map<String, String> map = new HashMap<String, String>();
		JobDetails jobDetail = new JobDetails();
		jobDetail.setJobName("job1");
		jobDetail.setJobGroup("group1");
		jobDetail.setIsDurable(false);
		jobDetail.setJobDescription("abc");
		jobDetail.setJobMapData(map);
		
		
		Mockito.when(scheduler.checkExists(any(JobKey.class))).thenReturn(true);
		Mockito.when(scheduler.getJobDetail(any(JobKey.class))).thenReturn(sch_jobDetail);
		Mockito.when(sch_jobDetail.getJobBuilder()).thenReturn(jobBuilder);
		Mockito.when(jobBuilder.storeDurably(anyBoolean())).thenReturn(jobBuilder);
		Mockito.when(jobBuilder.withDescription(anyString())).thenReturn(jobBuilder);
		Mockito.when(jobBuilder.build()).thenReturn(sch_jobDetail);
	
		Assertions.assertThat(schedulerService.UpdateJob(jobDetail)).isEqualTo(true);
	}
	
	@Test
	public void CreateJob_JobDoesNotExist_ReturnTrue() throws SchedulerException, ClassNotFoundException
	{
		JobBuilder jobBuilder = mock(JobBuilder.class);
		Map<String, String> map = new HashMap<String, String>();
		JobDetails details= new JobDetails();
		details.setJobName("job1");
		details.setJobGroup("group1");
		details.setJobClass("com.nfinity.fastcode.jobs.Job1");
		details.setIsDurable(true);
        details.setJobDescription("abc");
        details.setJobMapData(map);
      
        Mockito.when(scheduler.checkExists(any(JobKey.class))).thenReturn(false);
        Mockito.when(jobBuilder.storeDurably(details.getIsDurable())).thenReturn(jobBuilder);
        Mockito.when(jobBuilder.withDescription(anyString())).thenReturn(jobBuilder);
        Mockito.when(jobBuilder.withIdentity(anyString(), anyString())).thenReturn(jobBuilder);
		Mockito.when(jobBuilder.build()).thenReturn(sch_jobDetail);
        	
        Assertions.assertThat(schedulerService.CreateJob(details)).isEqualTo(true);
	}

	@Test
	public void CreateJob_JobExists_ReturnFalse() throws SchedulerException, ClassNotFoundException
	{
		JobDetails details= new JobDetails();
		details.setJobName("job1");
		details.setJobGroup("group1");
	
        Mockito.when(scheduler.checkExists(any(JobKey.class))).thenReturn(true);
        Assertions.assertThat(schedulerService.CreateJob(details)).isEqualTo(false);
	}

	@Test
	public void setJobMapData_JobExists_ReturnJobDetails()
	{
		Map<String, String> map = new HashMap<String, String>();    
		Assertions.assertThat(schedulerService.setJobMapData(sch_jobDetail, map)).isEqualTo(sch_jobDetail);
	}
	
	@Test
	public void CancelTrigger_TriggerDoesNotExist_ReturnFalse() throws SchedulerException
	{
		Mockito.when(scheduler.checkExists(any(TriggerKey.class))).thenReturn(false);
		Assertions.assertThat(schedulerService.CancelTrigger("trigger1", "group1")).isEqualTo(false);
	}
	
	@Test
	public void CancelTrigger_TriggerExists_TriggerCancelled() throws SchedulerException
	{
		Mockito.when(scheduler.checkExists(any(TriggerKey.class))).thenReturn(true);
		Assertions.assertThat(schedulerService.CancelTrigger("trigger1", "group1")).isEqualTo(true);
	}
	
	@Test
	public void ReturnTrigger_TriggerExistsAndTriggerTypeIsSimple_ReturnTriggerOutput() throws SchedulerException
	{
		JobKey jobKey = new JobKey("job1","group1");
		TriggerState triggerState= TriggerState.NONE ;
		SimpleTriggerImpl trigg = mock(SimpleTriggerImpl.class);
		
		JobDataMap mapData =new JobDataMap();
		GetTriggerOutput obj = new GetTriggerOutput();

		Mockito.when(scheduler.getTrigger(any(TriggerKey.class))).thenReturn(trigg);
		Mockito.when(scheduler.checkExists(any(TriggerKey.class))).thenReturn(true);
		Mockito.when(trigg.getJobKey()).thenReturn(jobKey);
		Mockito.when(scheduler.getJobDetail(any(JobKey.class))).thenReturn(sch_jobDetail);
		Mockito.when(sch_jobDetail.getJobDataMap()).thenReturn(mapData);
	
		Mockito.when(scheduler.getTrigger(any(TriggerKey.class))).thenReturn(trigg);
		Mockito.when(trigg.getJobDataMap()).thenReturn(mapData);
		
		Mockito.when(scheduler.getTriggerState(any(TriggerKey.class))).thenReturn(triggerState);
	
		Assertions.assertThat(schedulerService.ReturnTrigger("trigger1", "group1")).isEqualToIgnoringNullFields(obj);
	}
	
	@Test
	public void ReturnTrigger_TriggerExistsAndTriggerTypeIsCron_ReturnTriggerOutput() throws SchedulerException
	{
		JobKey jobKey = new JobKey("job1","group1");
		TriggerState triggerState= TriggerState.NONE ;
		JobDetailImpl detail = new JobDetailImpl();
		detail.setJobClass(sampleJob.class);
		detail.setName("job1");
		detail.setGroup("group1");

		Trigger trigger1 = TriggerBuilder.newTrigger().forJob(detail)
				.withIdentity("trigger1", "group1")
				.startAt(new Date())
				.withSchedule(CronScheduleBuilder.cronSchedule("0 0/5 14 * * ?"))
				.build();

		JobDataMap mapData =new JobDataMap();
		GetTriggerOutput obj = new GetTriggerOutput();

		Mockito.when(scheduler.getTrigger(any(TriggerKey.class))).thenReturn(trigger1);
		Mockito.when(scheduler.checkExists(any(TriggerKey.class))).thenReturn(true);
		Mockito.when(trigger.getJobKey()).thenReturn(jobKey);
		Mockito.when(scheduler.getJobDetail(any(JobKey.class))).thenReturn(sch_jobDetail);
		Mockito.when(sch_jobDetail.getJobDataMap()).thenReturn(mapData);
	
		Mockito.when(scheduler.getTrigger(any(TriggerKey.class))).thenReturn(trigger1);
		Mockito.when(trigger.getJobDataMap()).thenReturn(mapData);
		
		Mockito.when(scheduler.getTriggerState(any(TriggerKey.class))).thenReturn(triggerState);
	
		Assertions.assertThat(schedulerService.ReturnTrigger("trigger1", "group1")).isEqualToIgnoringNullFields(obj);
	}
	
	@Test
	public void ReturnTrigger_TriggerDoesNotExist_ReturnNull() throws SchedulerException
	{
		Mockito.when(scheduler.checkExists(any(TriggerKey.class))).thenReturn(false);
		Assertions.assertThat(schedulerService.ReturnTrigger("trigger1", "group1")).isEqualTo(null);
	}
	
	@Test
	public void PauseTrigger_TriggerDoesNotExist_ReturnFalse() throws SchedulerException
	{
		Mockito.when(scheduler.checkExists(any(TriggerKey.class))).thenReturn(false);
		Assertions.assertThat(schedulerService.PauseTrigger("trigger1", "group1")).isEqualTo(false);
	}
	
	@Test
	public void PauseTrigger_TriggerExists_TriggerPaused() throws SchedulerException
	{
		Mockito.when(scheduler.checkExists(any(TriggerKey.class))).thenReturn(true);
		Assertions.assertThat(schedulerService.PauseTrigger("trigger1", "group1")).isEqualTo(true);
	}
	
	@Test
	public void ResumeTrigger_TriggerDoesNotExist_ReturnFalse() throws SchedulerException
	{
		Mockito.when(scheduler.checkExists(any(TriggerKey.class))).thenReturn(false);
		Assertions.assertThat(schedulerService.ResumeTrigger("trigger1", "group1")).isEqualTo(false);
	}
	
	@Test
	public void ResumeTrigger_TriggerExists_TriggerPaused() throws SchedulerException
	{
		Mockito.when(scheduler.checkExists(any(TriggerKey.class))).thenReturn(true);
		Assertions.assertThat(schedulerService.ResumeTrigger("trigger1", "group1")).isEqualTo(true);
	}
	
	@Test
	public void CreateTrigger_TriggerDoesNotExistAndTriggerTypeIsSimple_ReturnTrue() throws SchedulerException
	{
		TriggerCreationDetails obj= new TriggerCreationDetails();
		Map<String, String> map = new HashMap<String, String>();    
		obj.setTriggerMapData(map);
		obj.setTriggerName("trigger1");
		obj.setTriggerGroup("group1");
		obj.setJobName("job1");
		obj.setJobGroup("group1");
		obj.setTriggerType("Simple");
		Mockito.when(scheduler.checkExists(any(JobKey.class))).thenReturn(true);
		Mockito.when(scheduler.getJobDetail(any(JobKey.class))).thenReturn(sch_jobDetail);
		Mockito.when(scheduler.checkExists(any(TriggerKey.class))).thenReturn(false);
		Mockito.when(schedulerServiceUtil.CreateSimpleTrigger(any(TriggerCreationDetails.class),any(JobDetail.class))).thenReturn(trigger);
		
		Assertions.assertThat(schedulerService.CreateTrigger(obj)).isEqualTo(true);
	}
	
	@Test
	public void CreateTrigger_TriggerDoesNotExistAndTriggerTypeIsCron_ReturnTrue() throws SchedulerException
	{
		TriggerCreationDetails obj= new TriggerCreationDetails();
		Map<String, String> map = new HashMap<String, String>();    
		obj.setTriggerMapData(map);
		obj.setTriggerName("trigger1");
		obj.setTriggerGroup("group1");
		obj.setJobName("job1");
		obj.setJobGroup("group1");
		obj.setTriggerType("Cron");
		Mockito.when(scheduler.checkExists(any(JobKey.class))).thenReturn(true);
		Mockito.when(scheduler.getJobDetail(any(JobKey.class))).thenReturn(sch_jobDetail);
		Mockito.when(scheduler.checkExists(any(TriggerKey.class))).thenReturn(false);
		Mockito.when(schedulerServiceUtil.CreateCronTrigger(any(TriggerCreationDetails.class),any(JobDetail.class))).thenReturn(trigger);
		
		Assertions.assertThat(schedulerService.CreateTrigger(obj)).isEqualTo(true);
	}
	
	@Test
	public void CreateTrigger_TriggerAlreadyExists_ReturnFalse() throws SchedulerException
	{
		TriggerCreationDetails obj= new TriggerCreationDetails();
		obj.setTriggerName("trigger1");
		obj.setTriggerGroup("group1");
		obj.setJobName("job1");
		obj.setJobGroup("group1");
		obj.setTriggerType("Cron");
		Mockito.when(scheduler.checkExists(any(JobKey.class))).thenReturn(true);
		Mockito.when(scheduler.getJobDetail(any(JobKey.class))).thenReturn(sch_jobDetail);
		Mockito.when(scheduler.checkExists(any(TriggerKey.class))).thenReturn(true);
		Assertions.assertThat(schedulerService.CreateTrigger(obj)).isEqualTo(false);
	}
	
	@Test
	public void CreateTrigger_JobDoesNotExist_ReturnFalse() throws SchedulerException
	{
		TriggerCreationDetails obj= new TriggerCreationDetails();
		obj.setTriggerName("trigger1");
		obj.setTriggerGroup("group1");
		obj.setJobName("job1");
		obj.setJobGroup("group1");
		obj.setTriggerType("Cron");
		Mockito.when(scheduler.checkExists(any(JobKey.class))).thenReturn(false);
		Assertions.assertThat(schedulerService.CreateTrigger(obj)).isEqualTo(false);
	}

	@Test
	public void setJobMapDataInTrigger_TriggerExists_ReturnTriggerObject()
	{
		Map<String, String> map = new HashMap<String, String>();    
		Assertions.assertThat(schedulerService.setJobMapDataInTrigger(trigger, map)).isEqualTo(trigger);
	}
	
	@Test
	public void ListAllTriggerGroups_GetGroupsFromScheduler_ReturnList() throws SchedulerException
	{
		List<String> list = new ArrayList<String>();
        Mockito.when(scheduler.getTriggerGroupNames()).thenReturn(list);
        Assertions.assertThat(schedulerService.ListAllTriggerGroups()).isEqualTo(list);
	}
	
	@Test
	public void ListAllTriggers_TriggerExists_ReturnList() throws Exception
	{
		List<TriggerDetailsEntity> list = new ArrayList<>();
		TriggerDetailsEntity triggerDetails=mock (TriggerDetailsEntity.class);
		list.add(triggerDetails);
		Pageable pageable = mock(Pageable.class);
		Page<TriggerDetailsEntity> foundTriggers =new PageImpl(list);
		SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");
		
		Mockito.when(triggerDetailsManager.FindAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundTriggers);
		
		Assertions.assertThat(schedulerService.ListAllTriggers(search,pageable)).isNotNull();
	}
	
	@Test
	public void  searchAllPropertiesForTriggerDetail_SearchIsNotNull_ReturnBooleanBuilder() throws ParseException
	{
		String search= "xyz";
		String operator= "equals";
		QTriggerDetailsEntity triggers = QTriggerDetailsEntity.triggerDetailsEntity;
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(triggers.jobName.eq(search));
		builder.or(triggers.jobGroup.eq(search));
		builder.or(triggers.triggerGroup.eq(search));
		builder.or(triggers.triggerName.eq(search));
		builder.or(triggers.triggerState.eq(search));
		builder.or(triggers.triggerType.eq(search));

		Assertions.assertThat(schedulerService.searchAllPropertiesForTriggerDetails(triggers,search,operator)).isEqualTo(builder);
	}

	@Test
	public void searchSpecificPropertyForTriggerDetail_PropertyExists_ReturnBooleanBuilder() throws Exception
	{
		String operator= "equals";
		List<String> list = new ArrayList<>();
		list.add("jobName");

		QTriggerDetailsEntity triggers = QTriggerDetailsEntity.triggerDetailsEntity;
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(triggers.jobName.eq("xyz"));	

		Assertions.assertThat(schedulerService.searchSpecificPropertyForTriggerDetails(triggers,list,"xyz",operator)).isEqualTo(builder);
	}
	
	@Test
	public void searchKeyValuePairForTriggerDetail_PropertyExists_ReturnBooleanBuilder()
	{
		QTriggerDetailsEntity triggers = QTriggerDetailsEntity.triggerDetailsEntity;
		SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
		Map<String,SearchFields> map = new HashMap();
		map.put("jobName", searchFields);
		
        BooleanBuilder builder = new BooleanBuilder();
		builder.and(triggers.jobName.eq("xyz"));
        
        Assertions.assertThat(schedulerService.searchKeyValuePairForTriggerDetails(triggers, map)).isEqualTo(builder);
	}

	@Test(expected = Exception.class)
	public void checkPropertiesForTriggerDetail_PropertyDoesNotExist_ThrowException() throws Exception
	{
		List<String> list = new ArrayList<>();
		list.add("test");

		schedulerService.checkPropertiesForTriggerDetails(list);
	}
	@Test
	public void checkPropertiesForTriggerDetail_PropertyExists_ReturnNothing() throws Exception
	{
		List<String> list = new ArrayList<>();
		list.add("jobName");

		schedulerService.checkPropertiesForTriggerDetails(list);
	}
	
	@Test
	public void searchTriggerDetail_SearchIsNotNullAndSearchContainsCaseOne_ReturnBooleanBuilder() throws Exception
	{
		QTriggerDetailsEntity triggers = QTriggerDetailsEntity.triggerDetailsEntity;
		SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");
        BooleanBuilder builder = new BooleanBuilder();
		builder.or(triggers.jobName.eq("xyz"));
		builder.or(triggers.jobGroup.eq("xyz"));
		builder.or(triggers.triggerGroup.eq("xyz"));
		builder.or(triggers.triggerName.eq("xyz"));
		builder.or(triggers.triggerState.eq("xyz"));
		builder.or(triggers.triggerType.eq("xyz"));
        
        Assertions.assertThat(schedulerService.SearchTriggerDetails(search)).isEqualTo(builder);
        
	}
	
	@Test
	public void searchTriggerDetail_SearchIsNotNullAndSearchContainsCaseTwo_ReturnBooleanBuilder() throws Exception
	{
		QTriggerDetailsEntity triggers = QTriggerDetailsEntity.triggerDetailsEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(2);
		search.setValue("xyz");
		search.setOperator("equals");
		fields.setFieldName("jobName");
        fieldsList.add(fields);
        search.setFields(fieldsList);
    	BooleanBuilder builder = new BooleanBuilder();
    	builder.or(triggers.jobName.eq("xyz"));

        Assertions.assertThat(schedulerService.SearchTriggerDetails(search)).isEqualTo(builder);
        
	}
	
	@Test
	public void searchTriggerDetail_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception
	{
		Map<String,SearchFields> map = new HashMap<>();
		QTriggerDetailsEntity triggers = QTriggerDetailsEntity.triggerDetailsEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		fields.setFieldName("jobName");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
    	BooleanBuilder builder = new BooleanBuilder();
    	builder.or(triggers.jobName.eq("xyz"));
		
        Assertions.assertThat(schedulerService.SearchTriggerDetails(search)).isEqualTo(builder);
        
	}
	
	@Test
	public void searchTriggerDetail_StringIsNull_ReturnNull() throws Exception
	{
		 Assertions.assertThat(schedulerService.SearchTriggerDetails(null)).isEqualTo(null);
	}
	
	@Test
	public void millisToDate_InputInMillis_ReturnDate() throws ParseException
	{
		Date date = new Date(2);
		Assertions.assertThat(schedulerService.millisToDate(2)).isEqualTo(date);
	}
	
	@Test
	public void ExceutionHistoryByJob_JobNameAndGroupIsNotNull_ReturnList()
	{
		List<JobHistoryEntity> jobList=new ArrayList();
		List<GetJobOutput> output = new ArrayList();
		
		Mockito.when(jobHistoryManager.FindByJob(anyString(), anyString())).thenReturn(jobList);
	    Mockito.when(schedulerServiceUtil.ReturnJobOutput(jobList)).thenReturn(output);
	    
	    Assertions.assertThat(schedulerService.ExecutionHistoryByJob("job1", "group1")).isEqualTo(output);
	}
	
	@Test
	public void ExceutionHistoryTrigger_TriggerNameAndGroupIsNotNull_ReturnList()
	{
		List<JobHistoryEntity> jobList=new ArrayList();
		List<GetJobOutput> output = new ArrayList();
		
		Mockito.when(jobHistoryManager.FindByTrigger(anyString(), anyString())).thenReturn(jobList);
	    Mockito.when(schedulerServiceUtil.ReturnJobOutput(jobList)).thenReturn(output);
	    
	    Assertions.assertThat(schedulerService.ExecutionHistoryByJob("trigger1", "group1")).isEqualTo(output);
	}
	
	@Test
	public void ExecutionHistory_JobsExists_ReturnList() throws Exception
	{
		List<JobHistoryEntity> list=new ArrayList<>();
	    JobHistoryEntity jobHistory=mock (JobHistoryEntity.class);
		List<GetJobOutput> output = new ArrayList();
		Pageable pageable = mock(Pageable.class);
		Page<JobHistoryEntity> foundPage = new PageImpl(list);
		SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");
		
		
		Mockito.when(jobHistoryManager.FindAll(any(Predicate.class), any(Pageable.class))).thenReturn(foundPage);
		Mockito.when(schedulerServiceUtil.ReturnJobOutput(list)).thenReturn(output);
	    Assertions.assertThat(schedulerService.ExecutionHistory(search,pageable)).isNotNull();
	}

	@Test
	public void  searchAllPropertiesForExecutionHistory_SearchIsNotNull_ReturnBooleanBuilder() throws ParseException
	{
		String search= "xyz";
		String operator= "equals";
		QJobHistoryEntity jobs = QJobHistoryEntity.jobHistoryEntity;
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(jobs.jobName.eq(search));
		builder.or(jobs.jobGroup.eq(search));
		builder.or(jobs.triggerGroup.eq(search));
		builder.or(jobs.triggerName.eq(search));
		
		Assertions.assertThat(schedulerService.searchAllPropertiesForJobHistory(jobs,search,operator)).isEqualTo(builder);
	}

	@Test
	public void searchSpecificPropertyForExecutionHistory_PropertyExists_ReturnBooleanBuilder() throws Exception
	{
		String operator= "equals";
		List<String> list = new ArrayList<>();
		list.add("jobName");
		
		QJobHistoryEntity jobs = QJobHistoryEntity.jobHistoryEntity;
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(jobs.jobName.eq("xyz"));	

		Assertions.assertThat(schedulerService.searchSpecificPropertyForJobHistory(jobs,list,"xyz",operator)).isEqualTo(builder);

	}
	
	@Test
	public void searchKeyValuePairForExecutionHistory_PropertyExists_ReturnBooleanBuilder()
	{
		QJobHistoryEntity jobs = QJobHistoryEntity.jobHistoryEntity;
		SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
		Map<String,SearchFields> map = new HashMap();
		map.put("jobName", searchFields);
		
        BooleanBuilder builder = new BooleanBuilder();
		builder.and(jobs.jobName.eq("xyz"));
		
        
        Assertions.assertThat(schedulerService.searchKeyValuePairForJobHistory(jobs, map)).isEqualTo(builder);
	}

	@Test(expected = Exception.class)
	public void checkPropertiesForExecutionHistory_PropertyDoesNotExist_ThrowException() throws Exception
	{
		List<String> list = new ArrayList<>();
		list.add("test");

		schedulerService.checkPropertiesForJobHistory(list);
	}
	@Test
	public void checkPropertiesForExecutionHistory_PropertyExists_ReturnNothing() throws Exception
	{
		List<String> list = new ArrayList<>();
		list.add("jobName");

		schedulerService.checkPropertiesForJobHistory(list);
	}
	
	@Test
	public void searchExecutionHistory_SearchIsNotNullAndSearchContainsCaseOne_ReturnBooleanBuilder() throws Exception
	{
		QJobHistoryEntity jobs = QJobHistoryEntity.jobHistoryEntity;
		SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");
        BooleanBuilder builder = new BooleanBuilder();
        builder.or(jobs.jobName.eq("xyz"));
		builder.or(jobs.jobGroup.eq("xyz"));
		builder.or(jobs.triggerGroup.eq("xyz"));
     	builder.or(jobs.triggerName.eq("xyz"));
        
        Assertions.assertThat(schedulerService.SearchExecutionHistory(search)).isEqualTo(builder);
        
	}
	
	@Test
	public void searchExecutionHistory_SearchIsNotNullAndSearchContainsCaseTwo_ReturnBooleanBuilder() throws Exception
	{
		QJobHistoryEntity jobs = QJobHistoryEntity.jobHistoryEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(2);
		search.setValue("xyz");
		search.setOperator("equals");
		fields.setFieldName("jobName");
        fieldsList.add(fields);
        search.setFields(fieldsList);
    	BooleanBuilder builder = new BooleanBuilder();
    	builder.or(jobs.jobName.eq("xyz"));
		
        Assertions.assertThat(schedulerService.SearchExecutionHistory(search)).isEqualTo(builder);
        
	}
	
	@Test
	public void searchExecutionHistory_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception
	{
		Map<String,SearchFields> map = new HashMap<>();
		QJobHistoryEntity jobs = QJobHistoryEntity.jobHistoryEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		fields.setFieldName("jobName");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
    	BooleanBuilder builder = new BooleanBuilder();
    	builder.and(jobs.jobName.eq("xyz"));
		
        Assertions.assertThat(schedulerService.SearchExecutionHistory(search)).isEqualTo(builder);
        
	}
	
	@Test
	public void searchExecutionHistory_StringIsNull_ReturnNull() throws Exception
	{
		 Assertions.assertThat(schedulerService.SearchExecutionHistory(null)).isEqualTo(null);
	}
	
	

}

