package com.nfinity.example179.Scheduler.SchedulerService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
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
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.slf4j.Logger;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nfinity.example179.Scheduler.application.Dto.JobDetails;
import com.nfinity.example179.Scheduler.application.Dto.TriggerCreationDetails;
import com.nfinity.example179.Scheduler.application.Dto.TriggerDetails;
import com.nfinity.example179.Scheduler.domain.model.JobHistoryEntity;
import com.nfinity.example179.Scheduler.jobs.sampleJob;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;


@RunWith(SpringJUnit4ClassRunner.class)
public class SchedulerServiceUtilTest {

	@InjectMocks
	private SchedulerServiceUtil schedulerServiceUtil = new SchedulerServiceUtil();

	@Mock
	private SchedulerFactoryBean schedulerFactoryBean;

	@Mock
	private Scheduler mocked_Scheduler;

	@Mock
	private JobExecutionContext executionContext;

	@Mock
	private JobDetail sch_jobDetail;

	@Mock
	private Trigger mocked_Trigger;
	
	@Mock
	private TriggerBuilder triggerBuilder;


	@Mock
	private Logger loggerMock;

	@Mock
	private LoggingHelper logHelper;

	private JobDetailImpl detail;
	private Trigger trigger; 

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(schedulerServiceUtil);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
		Mockito.when(schedulerServiceUtil.getScheduler()).thenReturn(mocked_Scheduler);
		JobDataMap mapData = new JobDataMap();
		mapData.put("test", "mapData");
		
		detail = new JobDetailImpl();
		detail.setJobClass(sampleJob.class);
		detail.setName("job1");
		detail.setGroup("group1");
		detail.setJobClass(sampleJob.class);
		detail.setJobDataMap(mapData);
		detail.setDurability(true);

		trigger= TriggerBuilder.newTrigger().forJob(detail)
				.withIdentity("trigger1","group1")
				.withDescription("test")
				.startAt(new Date()).withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withRepeatCount(10).withIntervalInSeconds(300))
				.build();
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void ReturnTriggerDetails_TriggerIsNotNull_ReturnDetails() throws SchedulerException {
		JobDataMap mapData = new JobDataMap();
		TriggerDetails triggDetails = mock(TriggerDetails.class);
		TriggerKey triggerKey=new TriggerKey("trigger1" , "group1");

		Mockito.when(mocked_Trigger.getKey()).thenReturn(triggerKey);
		Mockito.when(mocked_Scheduler.getTriggerState(triggerKey)).thenReturn(TriggerState.NONE);
		Mockito.when(mocked_Scheduler.getTrigger(triggerKey)).thenReturn(mocked_Trigger);
		Mockito.when(mocked_Trigger.getJobDataMap()).thenReturn(mapData);

		Assertions.assertThat(schedulerServiceUtil.ReturnTriggerDetails(mocked_Trigger)).isEqualToIgnoringNullFields(triggDetails);
	}

	@Test
	public void ReturnJobDetails_JobKeyIsNotNull_ReturnDetails() throws SchedulerException
	{
		JobKey jobKey = new JobKey("job1", "group1");

		Map<String, String> map = new HashMap<String, String>();
		map.put("test", "mapData");
		JobDetails jobDetails = new JobDetails();
		jobDetails.setTriggerDetails(null);
		jobDetails.setIsDurable(true);
		jobDetails.setJobMapData(map);

		Mockito.when(mocked_Scheduler.getJobDetail(jobKey)).thenReturn(detail);

		Assertions.assertThat(schedulerServiceUtil.ReturnJobDetails(jobKey)).isEqualToIgnoringNullFields(jobDetails);
	}

	@Test
	public void IsJobRunning_JobNameAndGroupIsNotNull_ReturnTrue() throws SchedulerException
	{
		List<JobExecutionContext> executingJobs = new ArrayList<>();
		executingJobs.add(executionContext);

		Mockito.when(mocked_Scheduler.getCurrentlyExecutingJobs()).thenReturn(executingJobs);
		Mockito.when(executionContext.getJobDetail()).thenReturn(detail);

		Assertions.assertThat(schedulerServiceUtil.isJobRunning("job1", "group1")).isEqualTo(true);

	}

	private Scheduler setTriggerForJob() throws Exception
	{
		SchedulerFactoryBean bean = new SchedulerFactoryBean();
		bean.setJobFactory(new SpringBeanJobFactory());
		bean.setTriggers(trigger);
		bean.setJobDetails(detail);
		bean.afterPropertiesSet();
        bean.start();
        return bean.getObject();
	}
	
	@Test
	public void JobState_JobNameAndGroupIsNotNullTriggerStatePaused_ReturnJobState() throws Exception
	{
        List<? extends Trigger> list = setTriggerForJob().getTriggersOfJob(detail.getKey());
		Mockito.when(mocked_Scheduler.getJobDetail(any(JobKey.class))).thenReturn(detail);
		Mockito.doReturn(list).when(mocked_Scheduler).getTriggersOfJob(any(JobKey.class));
		Mockito.when(mocked_Scheduler.getTriggerState(trigger.getKey())).thenReturn(TriggerState.PAUSED);
		Assertions.assertThat(schedulerServiceUtil.getJobState("job1", "group1")).isEqualTo("PAUSED");

	}
	@Test
	public void JobState_JobNameAndGroupIsNotNullTriggerStateBlocked_ReturnJobState() throws Exception
	{
        List<? extends Trigger> list = setTriggerForJob().getTriggersOfJob(detail.getKey());
		Mockito.when(mocked_Scheduler.getJobDetail(any(JobKey.class))).thenReturn(detail);
		Mockito.doReturn(list).when(mocked_Scheduler).getTriggersOfJob(any(JobKey.class));
		Mockito.when(mocked_Scheduler.getTriggerState(trigger.getKey())).thenReturn(TriggerState.BLOCKED);
		Assertions.assertThat(schedulerServiceUtil.getJobState("job1", "group1")).isEqualTo("BLOCKED");

	}
	@Test
	public void JobState_JobNameAndGroupIsNotNullTriggerStateComplete_ReturnJobState() throws Exception
	{
        List<? extends Trigger> list = setTriggerForJob().getTriggersOfJob(detail.getKey());
		Mockito.when(mocked_Scheduler.getJobDetail(any(JobKey.class))).thenReturn(detail);
		Mockito.doReturn(list).when(mocked_Scheduler).getTriggersOfJob(any(JobKey.class));
		Mockito.when(mocked_Scheduler.getTriggerState(trigger.getKey())).thenReturn(TriggerState.COMPLETE);
		Assertions.assertThat(schedulerServiceUtil.getJobState("job1", "group1")).isEqualTo("COMPLETE");

	}
	@Test
	public void JobState_JobNameAndGroupIsNotNullTriggerStateError_ReturnJobState() throws Exception
	{
        List<? extends Trigger> list = setTriggerForJob().getTriggersOfJob(detail.getKey());
		Mockito.when(mocked_Scheduler.getJobDetail(any(JobKey.class))).thenReturn(detail);
		Mockito.doReturn(list).when(mocked_Scheduler).getTriggersOfJob(any(JobKey.class));
		Mockito.when(mocked_Scheduler.getTriggerState(trigger.getKey())).thenReturn(TriggerState.ERROR);
		Assertions.assertThat(schedulerServiceUtil.getJobState("job1", "group1")).isEqualTo("ERROR");

	}
	
	@Test
	public void JobState_JobNameAndGroupIsNotNullTriggerStateNone_ReturnJobState() throws Exception
	{
        List<? extends Trigger> list = setTriggerForJob().getTriggersOfJob(detail.getKey());
		Mockito.when(mocked_Scheduler.getJobDetail(any(JobKey.class))).thenReturn(detail);
		Mockito.doReturn(list).when(mocked_Scheduler).getTriggersOfJob(any(JobKey.class));
		Mockito.when(mocked_Scheduler.getTriggerState(trigger.getKey())).thenReturn(TriggerState.NONE);
		Assertions.assertThat(schedulerServiceUtil.getJobState("job1", "group1")).isEqualTo("NONE");

	}
	@Test
	public void JobState_JobNameAndGroupIsNotNullTriggerStateNormal_ReturnJobState() throws Exception
	{
        List<? extends Trigger> list = setTriggerForJob().getTriggersOfJob(detail.getKey());
		Mockito.when(mocked_Scheduler.getJobDetail(any(JobKey.class))).thenReturn(detail);
		Mockito.doReturn(list).when(mocked_Scheduler).getTriggersOfJob(any(JobKey.class));
		Mockito.when(mocked_Scheduler.getTriggerState(trigger.getKey())).thenReturn(TriggerState.NORMAL);
		Assertions.assertThat(schedulerServiceUtil.getJobState("job1", "group1")).isEqualTo("SCHEDULED");

	}
	
	@Test
	public void JobState_JobNameAndGroupIsNotNullHavingNoTrigger_ReturnNull() throws Exception
	{
        List<? extends Trigger> list =new ArrayList();
		Mockito.when(mocked_Scheduler.getJobDetail(any(JobKey.class))).thenReturn(detail);
		Mockito.doReturn(list).when(mocked_Scheduler).getTriggersOfJob(any(JobKey.class));
		Mockito.when(mocked_Scheduler.getTriggerState(trigger.getKey())).thenReturn(TriggerState.PAUSED);
		Assertions.assertThat(schedulerServiceUtil.getJobState("job1", "group1")).isEqualTo(null);

	}

	@Test
	public void GetJobOutputDetails_JobEntityIsValid_ReturnList()
	{
		List<JobHistoryEntity> inputList = new ArrayList<JobHistoryEntity>();

		JobHistoryEntity jobHistoryEntity = mock(JobHistoryEntity.class);
		jobHistoryEntity.setId(1L);
		jobHistoryEntity.setJobName("job1");
		jobHistoryEntity.setJobGroup("group1");
		inputList.add(jobHistoryEntity);

		Assertions.assertThat(schedulerServiceUtil.ReturnJobOutput(inputList)).isNotEmpty();

	}

	@Test
	public void UpdateCronTrigger_TriggerExists_TriggerTrue() throws SchedulerException
	{
		TriggerKey triggerKey = new TriggerKey("trigger1","group1");
		TriggerCreationDetails obj=new TriggerCreationDetails();
		obj.setTriggerName("trigger1");
		obj.setTriggerGroup("group1");
		obj.setCronExpression("0/5 * * * * ?");

		Mockito.when(mocked_Scheduler.getTrigger(any(TriggerKey.class))).thenReturn(mocked_Trigger);
		Mockito.when(mocked_Trigger.getTriggerBuilder()).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.withSchedule(any(CronScheduleBuilder.class))).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.build()).thenReturn(mocked_Trigger);

		Assertions.assertThat(schedulerServiceUtil.UpdateCronTrigger(obj,triggerKey)).isEqualTo(true);
	}

	@Test
	public void UpdateSimpleTrigger_TriggerExistsRepeatIndefinteIsTrueAndRepeatIntervalIsNot_ReturnTrue() throws SchedulerException
	{
		TriggerKey triggerKey = new TriggerKey("trigger1","group1"); 
		TriggerCreationDetails obj=new TriggerCreationDetails();
		obj.setTriggerName("trigger1");
		obj.setTriggerGroup("group1");
		obj.setRepeatIndefinite(true);
		obj.setRepeatInterval(5);

		Mockito.when(mocked_Scheduler.getTrigger(any(TriggerKey.class))).thenReturn(mocked_Trigger);
		Mockito.when(mocked_Trigger.getTriggerBuilder()).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.withSchedule(any(SimpleScheduleBuilder.class))).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.build()).thenReturn(mocked_Trigger);

		Assertions.assertThat(schedulerServiceUtil.UpdateSimpleTrigger(obj,triggerKey)).isEqualTo(true);


	}
	@Test
	public void UpdateSimpleTrigger_TriggerExistsRepeatIndefinteIsFalseAndRepeatInterval_ReturnTrue() throws SchedulerException
	{
		TriggerKey triggerKey = new TriggerKey("trigger1","group1"); 
		TriggerCreationDetails obj=new TriggerCreationDetails();
		obj.setTriggerName("trigger1");
		obj.setTriggerGroup("group1");
		obj.setRepeatIndefinite(false);
		obj.setRepeatInterval(5);
		obj.setRepeatCount(5);

		Mockito.when(mocked_Scheduler.getTrigger(any(TriggerKey.class))).thenReturn(mocked_Trigger);
		Mockito.when(mocked_Trigger.getTriggerBuilder()).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.withSchedule(any(SimpleScheduleBuilder.class))).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.build()).thenReturn(mocked_Trigger);

		Assertions.assertThat(schedulerServiceUtil.UpdateSimpleTrigger(obj,triggerKey)).isEqualTo(true);


	}

	@Test
	public void UpdateSimpleTrigger_TriggerExistsExcecuteOnce_ReturnTrigger() throws SchedulerException
	{
		TriggerKey triggerKey = new TriggerKey("trigger1","group1");
		TriggerCreationDetails obj=new TriggerCreationDetails();
		obj.setTriggerName("trigger1");
		obj.setTriggerGroup("group1");

		Mockito.when(mocked_Scheduler.getTrigger(any(TriggerKey.class))).thenReturn(mocked_Trigger);
		Mockito.when(mocked_Trigger.getTriggerBuilder()).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.withSchedule(any(SimpleScheduleBuilder.class))).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.build()).thenReturn(mocked_Trigger);

		Assertions.assertThat(schedulerServiceUtil.UpdateSimpleTrigger(obj,triggerKey)).isEqualTo(true);

	}

	@Test
	public void CreateCronTrigger_TriggerDoesNotExistEndTimeIsNull_ReturnTrigger() throws SchedulerException
	{
		TriggerCreationDetails obj=new TriggerCreationDetails();
		obj.setTriggerName("trigger1");
		obj.setTriggerGroup("group1");
		obj.setCronExpression("0/5 * * * * ?");

		Trigger trigger1 = TriggerBuilder.newTrigger().forJob(detail)
				.withIdentity(obj.getTriggerName(), obj.getTriggerGroup())
				.withDescription(obj.getTriggerDescription())
				.startAt(new Date())
				.withSchedule(CronScheduleBuilder.cronSchedule(obj.getCronExpression()))
				.build();

		Mockito.when(mocked_Scheduler.getTrigger(any(TriggerKey.class))).thenReturn(mocked_Trigger);
		Mockito.when(mocked_Trigger.getTriggerBuilder()).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.withSchedule(any(CronScheduleBuilder.class))).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.build()).thenReturn(mocked_Trigger);

		Assertions.assertThat(schedulerServiceUtil.CreateCronTrigger(obj,detail)).isEqualTo(trigger1);

	}

	@Test
	public void CreateCronTrigger_TriggerDoesNotExistEndTimeIsNotNull_ReturnTrigger() throws SchedulerException
	{
		Date endTime = new Date();
		endTime = DateUtils.addMinutes(endTime, 5);
     	TriggerCreationDetails obj=new TriggerCreationDetails();
		obj.setTriggerName("trigger1");
		obj.setTriggerGroup("group1");
		obj.setEndTime(endTime);
		obj.setCronExpression("0/5 * * * * ?");

		Trigger trigger1 = TriggerBuilder.newTrigger().forJob(detail)
				.withIdentity(obj.getTriggerName(), obj.getTriggerGroup())
				.withDescription(obj.getTriggerDescription())
				.startAt(new Date())
				.endAt(endTime)
				.withSchedule(CronScheduleBuilder.cronSchedule(obj.getCronExpression()))
				.build();

		Mockito.when(mocked_Scheduler.getTrigger(any(TriggerKey.class))).thenReturn(mocked_Trigger);
		Mockito.when(mocked_Trigger.getTriggerBuilder()).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.withSchedule(any(CronScheduleBuilder.class))).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.build()).thenReturn(mocked_Trigger);

		Assertions.assertThat(schedulerServiceUtil.CreateCronTrigger(obj,detail)).isEqualTo(trigger1);

	}
	@Test
	public void CreateSimpleTrigger_TriggerDoesNotExistEndTimeIsNotNullSetRepeatIndefiniteAndInterval_ReturnTrigger() throws SchedulerException
	{
		Date endTime = new Date();
		endTime = DateUtils.addMinutes(endTime, 5);
		TriggerCreationDetails obj=new TriggerCreationDetails();
		obj.setTriggerName("trigger1");
		obj.setTriggerGroup("group1");
		obj.setEndTime(endTime);
		obj.setRepeatIndefinite(true);
		obj.setRepeatInterval(5);

		Trigger trigger1 = TriggerBuilder.newTrigger().forJob(detail)
				.withIdentity(obj.getTriggerName(), obj.getTriggerGroup())
				.withDescription(obj.getTriggerDescription())
				.startAt(new Date())
				.endAt(endTime).withSchedule(
						SimpleScheduleBuilder
						.simpleSchedule().repeatForever()
						.withIntervalInSeconds(obj.getRepeatInterval())
						).build();

		Mockito.when(mocked_Scheduler.getTrigger(any(TriggerKey.class))).thenReturn(mocked_Trigger);
		Mockito.when(mocked_Trigger.getTriggerBuilder()).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.withSchedule(any(SimpleScheduleBuilder.class))).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.build()).thenReturn(mocked_Trigger);

		Assertions.assertThat(schedulerServiceUtil.CreateSimpleTrigger(obj,detail)).isEqualTo(trigger1);

	}

	@Test
	public void CreateSimpleTrigger_TriggerDoesNotExistEndTimeIsNotNullSetRepeatCountAndInterval_ReturnTrigger() throws SchedulerException
	{
		Date endTime = new Date();
		endTime = DateUtils.addMinutes(endTime, 5);
		TriggerCreationDetails obj=new TriggerCreationDetails();
		obj.setTriggerName("trigger1");
		obj.setTriggerGroup("group1");
		obj.setEndTime(endTime);
		obj.setRepeatIndefinite(false);
		obj.setRepeatInterval(5);
		obj.setRepeatCount(5);

		Trigger trigger1 = TriggerBuilder.newTrigger().forJob(detail)
				.withIdentity(obj.getTriggerName(), obj.getTriggerGroup())
				.withDescription(obj.getTriggerDescription())
				.startAt(new Date())
				.endAt(endTime).withSchedule(
						SimpleScheduleBuilder
						.simpleSchedule().withRepeatCount(obj.getRepeatCount())
						.withIntervalInSeconds(obj.getRepeatInterval())
						).build();

		Mockito.when(mocked_Scheduler.getTrigger(any(TriggerKey.class))).thenReturn(mocked_Trigger);
		Mockito.when(mocked_Trigger.getTriggerBuilder()).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.withSchedule(any(SimpleScheduleBuilder.class))).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.build()).thenReturn(mocked_Trigger);

		Assertions.assertThat(schedulerServiceUtil.CreateSimpleTrigger(obj,detail)).isEqualTo(trigger1);

	}

	@Test
	public void CreateSimpleTrigger_TriggerDoesNotExistEndTimeIsNotNull_ReturnTrigger() throws SchedulerException
	{
		Date endTime = new Date();
		endTime = DateUtils.addMinutes(endTime, 5);
		TriggerCreationDetails obj=new TriggerCreationDetails();
		obj.setTriggerName("trigger1");
		obj.setTriggerGroup("group1");
		obj.setEndTime(endTime);

		Trigger trigger1 = TriggerBuilder.newTrigger().forJob(detail)
				.withIdentity(obj.getTriggerName(), obj.getTriggerGroup())
				.withDescription(obj.getTriggerDescription())
				.startAt(new Date())
				.endAt(endTime).withSchedule(
						SimpleScheduleBuilder
						.simpleSchedule())
				.build();

		Mockito.when(mocked_Scheduler.getTrigger(any(TriggerKey.class))).thenReturn(mocked_Trigger);
		Mockito.when(mocked_Trigger.getTriggerBuilder()).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.withSchedule(any(SimpleScheduleBuilder.class))).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.build()).thenReturn(mocked_Trigger);

		Assertions.assertThat(schedulerServiceUtil.CreateSimpleTrigger(obj,detail)).isEqualTo(trigger1);

	}

	@Test
	public void CreateSimpleTrigger_TriggerDoesNotExistEndTimeIsNullSetRepeatIndefiniteAndInterval_ReturnTrigger() throws SchedulerException
	{
		TriggerCreationDetails obj=new TriggerCreationDetails();
		obj.setTriggerName("trigger1");
		obj.setTriggerGroup("group1");
		obj.setRepeatIndefinite(true);
		obj.setRepeatInterval(5);

		Trigger trigger1 = TriggerBuilder.newTrigger().forJob(detail)
				.withIdentity(obj.getTriggerName(), obj.getTriggerGroup())
				.withDescription(obj.getTriggerDescription())
				.startAt(new Date()).withSchedule(
						SimpleScheduleBuilder
						.simpleSchedule().repeatForever()
						.withIntervalInSeconds(obj.getRepeatInterval())
						).build();

		Mockito.when(mocked_Scheduler.getTrigger(any(TriggerKey.class))).thenReturn(mocked_Trigger);
		Mockito.when(mocked_Trigger.getTriggerBuilder()).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.withSchedule(any(SimpleScheduleBuilder.class))).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.build()).thenReturn(mocked_Trigger);

		Assertions.assertThat(schedulerServiceUtil.CreateSimpleTrigger(obj,detail)).isEqualTo(trigger1);

	}

	@Test
	public void CreateSimpleTrigger_TriggerDoesNotExistEndTimeIsNullSetRepeatCountAndInterval_ReturnTrigger() throws SchedulerException
	{
		TriggerCreationDetails obj=new TriggerCreationDetails();
		obj.setTriggerName("trigger1");
		obj.setTriggerGroup("group1");
		obj.setRepeatIndefinite(false);
		obj.setRepeatInterval(5);
		obj.setRepeatCount(5);

		Trigger trigger1 = TriggerBuilder.newTrigger().forJob(detail)
				.withIdentity(obj.getTriggerName(), obj.getTriggerGroup())
				.withDescription(obj.getTriggerDescription()).startAt(new Date())
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withRepeatCount(obj.getRepeatCount())
						.withIntervalInSeconds(obj.getRepeatInterval())).build();

		Mockito.when(mocked_Scheduler.getTrigger(any(TriggerKey.class))).thenReturn(mocked_Trigger);
		Mockito.when(mocked_Trigger.getTriggerBuilder()).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.withSchedule(any(SimpleScheduleBuilder.class))).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.build()).thenReturn(mocked_Trigger);

		Assertions.assertThat(schedulerServiceUtil.CreateSimpleTrigger(obj,detail)).isEqualTo(trigger1);

	}

	@Test
	public void CreateSimpleTrigger_TriggerDoesNotExistEndTimeIsNull_ReturnTrigger() throws SchedulerException
	{
		TriggerCreationDetails obj=new TriggerCreationDetails();
		obj.setTriggerName("trigger1");
		obj.setTriggerGroup("group1");

		Trigger trigger1 = TriggerBuilder.newTrigger().forJob(detail)
				.withIdentity(obj.getTriggerName(), obj.getTriggerGroup())
				.withDescription(obj.getTriggerDescription())
				.startAt(new Date()).withSchedule(SimpleScheduleBuilder.simpleSchedule())
				.build();

		Mockito.when(mocked_Scheduler.getTrigger(any(TriggerKey.class))).thenReturn(mocked_Trigger);
		Mockito.when(mocked_Trigger.getTriggerBuilder()).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.withSchedule(any(SimpleScheduleBuilder.class))).thenReturn(triggerBuilder);
		Mockito.when(triggerBuilder.build()).thenReturn(mocked_Trigger);

		Assertions.assertThat(schedulerServiceUtil.CreateSimpleTrigger(obj,detail)).isEqualTo(trigger1);

	}

}
