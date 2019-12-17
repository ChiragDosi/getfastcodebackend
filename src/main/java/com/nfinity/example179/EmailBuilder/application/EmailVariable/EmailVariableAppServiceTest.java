package com.nfinity.example179.EmailBuilder.application.EmailVariable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.nfinity.example179.EmailBuilder.application.EmailVariable.Dto.*;
import com.nfinity.example179.EmailBuilder.domain.model.EmailVariableEntity;
import com.nfinity.example179.EmailBuilder.domain.model.QEmailVariableEntity;
import com.nfinity.example179.EmailBuilder.domain.EmailVariable.IEmailVariableManager;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class EmailVariableAppServiceTest {


	@InjectMocks
	EmailVariableAppService emailVariableAppService;
	
	@Mock
	private IEmailVariableManager emailVariableManager;
	
	@Mock
	private EmailVariableMapper emailVariableMapper;
	
	@Mock
    private Logger loggerMock;
   
	@Mock
	private LoggingHelper logHelper;

	private static long ID=15;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(emailVariableAppService);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	@Test 
	public void findEmailVariableById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
		
		Mockito.when(emailVariableManager.FindById(anyLong())).thenReturn(null);
		Assertions.assertThat(emailVariableAppService.FindById(ID)).isEqualTo(null);	
	 
	}

	@Test
	public void findEmailVariableById_IdIsNotNullAndEmailVariableExists_ReturnEmail() {

		EmailVariableEntity email = mock(EmailVariableEntity.class);

		Mockito.when(emailVariableManager.FindById(anyLong())).thenReturn(email);
		Assertions.assertThat(emailVariableAppService.FindById(ID)).isEqualTo(emailVariableMapper.EmailVariableEntityToCreateEmailVariableOutput(email));
	}

	@Test 
	public void findEmailVariableByName_NameIsNotNullAndEmailVariableDoesNotExist_ReturnNull() {
		
		Mockito.when(emailVariableManager.FindByName(anyString())).thenReturn(null);	
		Assertions.assertThat(emailVariableAppService.FindByName("Email1")).isEqualTo(null);	
	 
	}

	@Test
	public void findEmailVariableByName_NameVariableIsNotNullAndEmailVariableExists_ReturnEmailVariable() {

		EmailVariableEntity email = mock(EmailVariableEntity.class);

		Mockito.when(emailVariableManager.FindByName(anyString())).thenReturn(email);
		Assertions.assertThat(emailVariableAppService.FindByName("Email1")).isEqualTo(emailVariableMapper.EmailVariableEntityToCreateEmailVariableOutput(email));
	}


	@Test
	public void createEmailVariable_EmailVariableIsNotNullAndEmailVariableDoesNotExist_StoreEmailVariable() {

		EmailVariableEntity emailVariableEntity = mock(EmailVariableEntity.class);
		CreateEmailVariableInput email= mock(CreateEmailVariableInput.class);

		Mockito.when(emailVariableMapper.CreateEmailVariableInputToEmailVariableEntity(any(CreateEmailVariableInput.class))).thenReturn(emailVariableEntity);
		Mockito.when(emailVariableManager.Create(any(EmailVariableEntity.class))).thenReturn(emailVariableEntity);
		Assertions.assertThat(emailVariableAppService.Create(email)).isEqualTo(emailVariableMapper.EmailVariableEntityToCreateEmailVariableOutput(emailVariableEntity));
	}

	@Test
	public void deleteEmailVariable_EmailVariableIsNotNullAndEmailVariableExists_EmailVariableRemoved() {

		EmailVariableEntity email = mock(EmailVariableEntity.class);

		Mockito.when(emailVariableManager.FindById(anyLong())).thenReturn(email);
		emailVariableAppService.Delete(ID);
		verify(emailVariableManager).Delete(email);
	}


	@Test
	public void updateEmailVariable_EmailVariableIdIsNotNullAndEmailVariableExists_ReturnUpdatedEmailVariable() {

		EmailVariableEntity emailVariableEntity = mock(EmailVariableEntity.class);
		UpdateEmailVariableInput email=mock(UpdateEmailVariableInput.class);

		Mockito.when(emailVariableMapper.UpdateEmailVariableInputToEmailVariableEntity(any(UpdateEmailVariableInput.class))).thenReturn(emailVariableEntity);
		Mockito.when(emailVariableManager.Update(any(EmailVariableEntity.class))).thenReturn(emailVariableEntity);
		Assertions.assertThat(emailVariableAppService.Update(ID,email)).isEqualTo(emailVariableMapper.EmailVariableEntityToUpdateEmailVariableOutput(emailVariableEntity));

	}
	
	@Test
	public void Find_ListIsEmpty_ReturnList() throws Exception
	{
		List<EmailVariableEntity> list = new ArrayList<>();
		Page<EmailVariableEntity> foundPage = new PageImpl(list);
		Pageable pageable =mock(Pageable.class);

		List<FindEmailVariableByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");
		
		Mockito.when(emailVariableManager.FindAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(emailVariableAppService.Find(search,pageable)).isEqualTo(output);

	}

	@Test
	public void Find_ListIsNotEmpty_ReturnList() throws Exception
	{
		List<EmailVariableEntity> list = new ArrayList<>();
		EmailVariableEntity email=mock(EmailVariableEntity.class);
		list.add(email);
		Page<EmailVariableEntity> foundPage = new PageImpl(list);
		Pageable pageable =mock(Pageable.class);
		SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");

		List<FindEmailVariableByIdOutput> output = new ArrayList<>();
		output.add(emailVariableMapper.EmailVariableEntityToFindEmailVariableByIdOutput(email));
		Mockito.when(emailVariableManager.FindAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(emailVariableAppService.Find(search,pageable)).isEqualTo(output);

	}
	
	@Test
	public void  searchAllProperties_SearchIsNotNull_ReturnBooleanBuilder()
	{
		String search= "xyz";
		String operator= "equals";
		QEmailVariableEntity email = QEmailVariableEntity.emailVariableEntity;
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(email.propertyName.eq(search));
		builder.or(email.propertyType.eq(search));
		builder.or(email.defaultValue.eq(search));

		Assertions.assertThat(emailVariableAppService.searchAllProperties(email,search,operator)).isEqualTo(builder);
	}

	@Test
	public void searchSpecificProperty_PropertyExists_ReturnBooleanBuilder() throws Exception
	{
		String operator= "equals";
		List<String> list = new ArrayList<>();
		list.add("propertyName");
		list.add("propertyType");
		QEmailVariableEntity email = QEmailVariableEntity.emailVariableEntity;
		BooleanBuilder builder = new BooleanBuilder();
		builder.or(email.propertyName.eq("xyz"));
		builder.or(email.propertyType.eq("xyz"));
		
		Assertions.assertThat(emailVariableAppService.searchSpecificProperty(email,list,"xyz",operator)).isEqualTo(builder);

	}
	
	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder()
	{
		QEmailVariableEntity email = QEmailVariableEntity.emailVariableEntity;
		SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
		Map<String,SearchFields> map = new HashMap();
		map.put("propertyName", searchFields);
		
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(email.propertyName.eq("xyz"));
        
        Assertions.assertThat(emailVariableAppService.searchKeyValuePair(email, map)).isEqualTo(builder);
	}

	@Test(expected = Exception.class)
	public void checkProperties_PropertyDoesNotExist_ThrowException() throws Exception
	{
		List<String> list = new ArrayList<>();
		list.add("xyz");

		emailVariableAppService.checkProperties(list);
	}
	@Test
	public void checkProperties_PropertyExists_ReturnNothing() throws Exception
	{
		List<String> list = new ArrayList<>();
		list.add("propertyName");

		emailVariableAppService.checkProperties(list);
	}
	
	@Test
	public void search_SearchIsNotNullAndSearchContainsCaseOne_ReturnBooleanBuilder() throws Exception
	{
		QEmailVariableEntity email = QEmailVariableEntity.emailVariableEntity;
		SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");
        BooleanBuilder builder = new BooleanBuilder();
        builder.or(email.propertyName.eq("xyz"));
		builder.or(email.propertyType.eq("xyz"));
		builder.or(email.defaultValue.eq("xyz"));
        Assertions.assertThat(emailVariableAppService.Search(search)).isEqualTo(builder);
        
	}
	
	@Test
	public void search_SearchIsNotNullAndSearchContainsCaseTwo_ReturnBooleanBuilder() throws Exception
	{
		QEmailVariableEntity email = QEmailVariableEntity.emailVariableEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(2);
		search.setValue("xyz");
		search.setOperator("equals");
		fields.setFieldName("propertyName");
        fieldsList.add(fields);
        search.setFields(fieldsList);
    	BooleanBuilder builder = new BooleanBuilder();
    	builder.or(email.propertyName.eq("xyz"));

        Assertions.assertThat(emailVariableAppService.Search(search)).isEqualTo(builder);
        
	}
	@Test
	public void search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception
	{
		Map<String,SearchFields> map = new HashMap<>();
		QEmailVariableEntity email = QEmailVariableEntity.emailVariableEntity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue("xyz");
		search.setOperator("equals");
		fields.setFieldName("propertyName");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
    	BooleanBuilder builder = new BooleanBuilder();
    	builder.and(email.propertyName.eq("xyz"));
    	
        Assertions.assertThat(emailVariableAppService.Search(search)).isEqualTo(builder);
        
	}
	
	@Test
	public void search_StringIsNull_ReturnNull() throws Exception
	{
		 Assertions.assertThat(emailVariableAppService.Search(null)).isEqualTo(null);
	}
	
	
}
