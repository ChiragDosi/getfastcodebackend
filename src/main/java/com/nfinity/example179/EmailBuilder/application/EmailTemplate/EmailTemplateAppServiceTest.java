package com.nfinity.example179.EmailBuilder.application.EmailTemplate;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nfinity.example179.CommonModule.Search.SearchCriteria;
import com.nfinity.example179.CommonModule.Search.SearchFields;
import com.nfinity.example179.EmailBuilder.application.EmailTemplate.Dto.*;
import com.nfinity.example179.EmailBuilder.domain.*;
import com.nfinity.example179.EmailBuilder.domain.EmailTemplate.IEmailTemplateManager;
import com.nfinity.example179.EmailBuilder.domain.model.EmailTemplateEntity;
import com.nfinity.example179.EmailBuilder.domain.model.QEmailTemplateEntity;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class EmailTemplateAppServiceTest {

	@InjectMocks
	EmailTemplateAppService emailTemplateAppService;
	
	@Mock
	private IEmailTemplateManager emailTemplateManager;
	
	@Mock
	private EmailTemplateMapper emailTemplateMapper;
	
	@Mock
    private Logger loggerMock;
   
	@Mock
	private LoggingHelper logHelper;

	private static long ID=15;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(emailTemplateAppService);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test 
	public void findEmailById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
		
		Mockito.when(emailTemplateManager.FindById(anyLong())).thenReturn(null);
		Assertions.assertThat(emailTemplateAppService.FindById(ID)).isEqualTo(null);	 
	}

	@Test
	public void findEmailById_IdIsNotNullAndIdExists_ReturnEmail() {

		EmailTemplateEntity email = mock(EmailTemplateEntity.class);

		Mockito.when(emailTemplateManager.FindById(anyLong())).thenReturn(email);
		Assertions.assertThat(emailTemplateAppService.FindById(ID)).isEqualTo(emailTemplateMapper.EmailTemplateEntityToCreateEmailTemplateOutput(email));
	}

	@Test 
	public void findEmailByName_NameIsNotNullAndNameDoesNotExist_ReturnNull() {
		
		Mockito.when(emailTemplateManager.FindByName(anyString())).thenReturn(null);	
		Assertions.assertThat(emailTemplateAppService.FindByName("Email1")).isEqualTo(null);	
	}

	@Test
	public void findEmailByName_NameIsNotNullAndNameExists_ReturnEmail() {

		EmailTemplateEntity email = mock(EmailTemplateEntity.class);

		Mockito.when(emailTemplateManager.FindByName(anyString())).thenReturn(email);
		Assertions.assertThat(emailTemplateAppService.FindByName("Email1")).isEqualTo(emailTemplateMapper.EmailTemplateEntityToCreateEmailTemplateOutput(email));
	}

	@Test
	public void createEmail_EmailIsNotNullAndEmailDoesNotExist_StoreEmail() {

		EmailTemplateEntity emailTemplateEntity = mock(EmailTemplateEntity.class);
		CreateEmailTemplateInput email= mock(CreateEmailTemplateInput.class);

		Mockito.when(emailTemplateMapper.CreateEmailTemplateInputToEmailTemplateEntity(any(CreateEmailTemplateInput.class))).thenReturn(emailTemplateEntity);
		Mockito.when(emailTemplateManager.Create(any(EmailTemplateEntity.class))).thenReturn(emailTemplateEntity);
		Assertions.assertThat(emailTemplateAppService.Create(email)).isEqualTo(emailTemplateMapper.EmailTemplateEntityToCreateEmailTemplateOutput(emailTemplateEntity));
	}

	@Test
	public void deleteEmail_EmailIsNotNullAndEmailExists_EmailRemoved() {

		EmailTemplateEntity email = mock(EmailTemplateEntity.class);

		Mockito.when(emailTemplateManager.FindById(anyLong())).thenReturn(email);
		emailTemplateAppService.Delete(ID);
		verify(emailTemplateManager).Delete(email);
	}

	@Test
	public void updateEmail_EmailIdIsNotNullAndIdExists_ReturnUpdatedEmail() {

		EmailTemplateEntity emailTemplateEntity = mock(EmailTemplateEntity.class);
		UpdateEmailTemplateInput email=mock(UpdateEmailTemplateInput.class);

		Mockito.when(emailTemplateMapper.UpdateEmailTemplateInputToEmailTemplateEntity(any(UpdateEmailTemplateInput.class))).thenReturn(emailTemplateEntity);
		Mockito.when(emailTemplateManager.Update(any(EmailTemplateEntity.class))).thenReturn(emailTemplateEntity);
		Assertions.assertThat(emailTemplateAppService.Update(ID,email)).isEqualTo(emailTemplateMapper.EmailTemplateEntityToUpdateEmailTemplateOutput(emailTemplateEntity));
	}
	
	@Test
	public void Find_ListIsEmpty_ReturnList() throws Exception
	{
		List<EmailTemplateEntity> list = new ArrayList<>();
		Page<EmailTemplateEntity> foundPage = new PageImpl(list);
		Pageable pageable =mock(Pageable.class);

		List<FindEmailTemplateByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");

		Mockito.when(emailTemplateManager.FindAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(emailTemplateAppService.Find(search,pageable)).isEqualTo(output);
	}

	@Test
	public void Find_ListIsNotEmpty_ReturnList() throws Exception
	{
		List<EmailTemplateEntity> list = new ArrayList<>();
		EmailTemplateEntity emailTemplate=mock(EmailTemplateEntity.class);
		list.add(emailTemplate);
		Page<EmailTemplateEntity> foundPage = new PageImpl(list);
		Pageable pageable =mock(Pageable.class);
		SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");
		
		List<FindEmailTemplateByIdOutput> output = new ArrayList<>();
		output.add(emailTemplateMapper.EmailTemplateEntityToFindEmailTemplateByIdOutput(emailTemplate));
		Mockito.when(emailTemplateManager.FindAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(emailTemplateAppService.Find(search,pageable)).isEqualTo(output);
	}
	
	@Test
	public void  searchAllProperties_SearchIsNotNull_ReturnBooleanBuilder()
	{
		String search= "xyz";
		String operator= "equals";
		QEmailTemplateEntity emailTemplate = QEmailTemplateEntity.emailTemplateEntity;
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(emailTemplate.templateName.eq(search));
		builder.or(emailTemplate.category.eq(search));
		builder.or(emailTemplate.to.eq(search));
		builder.or(emailTemplate.cc.eq(search));
		builder.or(emailTemplate.bcc.eq(search));
		builder.or(emailTemplate.subject.eq(search));

		Assertions.assertThat(emailTemplateAppService.searchAllProperties(emailTemplate,search,operator)).isEqualTo(builder);
	}

	@Test
	public void searchSpecificProperty_PropertyExists_ReturnBooleanBuilder()
	{
		String operator= "equals";
		List<String> list = new ArrayList<>();
		list.add("templateName");
		list.add("category");
	
		QEmailTemplateEntity emailTemplate = QEmailTemplateEntity.emailTemplateEntity;
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(emailTemplate.templateName.eq("xyz"));
		builder.or(emailTemplate.category.eq("xyz"));

		Assertions.assertThat(emailTemplateAppService.searchSpecificProperty(emailTemplate,list,"xyz",operator)).isEqualTo(builder);
	}
	
	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder()
	{
		QEmailTemplateEntity emailTemplate = QEmailTemplateEntity.emailTemplateEntity;
		SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
		Map<String,SearchFields> map = new HashMap();
		map.put("templateName", searchFields);
		
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(emailTemplate.templateName.eq("xyz"));
        
        Assertions.assertThat(emailTemplateAppService.searchKeyValuePair(emailTemplate, map)).isEqualTo(builder);
	}

	public void checkProperties_PropertyDoesNotExist_ThrowException() throws Exception
	{
		List<String> list = new ArrayList<>();
		list.add("xyz");

		emailTemplateAppService.checkProperties(list);
	}
	
	@Test
	public void checkProperties_PropertyExists_ReturnNothing() throws Exception
	{
		List<String> list = new ArrayList<>();
		list.add("templateName");

		emailTemplateAppService.checkProperties(list);
	}
	
	@Test
	public void search_SearchIsNotNullAndSearchContainsCaseOne_ReturnBooleanBuilder() throws Exception
	{
		QEmailTemplateEntity emailTemplate = QEmailTemplateEntity.emailTemplateEntity;
		SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");
        BooleanBuilder builder = new BooleanBuilder();
        builder.or(emailTemplate.templateName.eq("xyz"));
		builder.or(emailTemplate.category.eq("xyz"));
		builder.or(emailTemplate.to.eq("xyz"));
		builder.or(emailTemplate.cc.eq("xyz"));
		builder.or(emailTemplate.bcc.eq("xyz"));
		builder.or(emailTemplate.subject.eq("xyz"));

        Assertions.assertThat(emailTemplateAppService.Search(search)).isEqualTo(builder);  
	}
	
	@Test
	public void search_SearchIsNotNullAndSearchContainsCaseTwo_ReturnBooleanBuilder() throws Exception
	{
		QEmailTemplateEntity emailTemplate = QEmailTemplateEntity.emailTemplateEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(2);
		search.setValue("xyz");
		search.setOperator("equals");
		fields.setFieldName("templateName");
        fieldsList.add(fields);
        search.setFields(fieldsList);
    	BooleanBuilder builder = new BooleanBuilder();
    	builder.or(emailTemplate.templateName.eq("xyz"));

        Assertions.assertThat(emailTemplateAppService.Search(search)).isEqualTo(builder);  
	}
	
	@Test
	public void search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception
	{
		Map<String,SearchFields> map = new HashMap<>();
		QEmailTemplateEntity emailTemplate = QEmailTemplateEntity.emailTemplateEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue("xyz");
		search.setOperator("equals");
		fields.setFieldName("templateName");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
    	BooleanBuilder builder = new BooleanBuilder();
    	builder.or(emailTemplate.templateName.eq("xyz"));
    	
        Assertions.assertThat(emailTemplateAppService.Search(search)).isEqualTo(builder);
	}
	
	@Test
	public void search_StringIsNull_ReturnNull() throws Exception
	{
		 Assertions.assertThat(emailTemplateAppService.Search(null)).isEqualTo(null);
	}
	
}