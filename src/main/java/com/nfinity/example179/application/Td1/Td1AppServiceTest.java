package com.nfinity.example179.application.Td1;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

import com.nfinity.example179.domain.Td1.*;
import com.nfinity.example179.CommonModule.Search.*;
import com.nfinity.example179.application.Td1.Dto.*;
import com.nfinity.example179.domain.model.QTd1Entity;
import com.nfinity.example179.domain.model.Td1Entity;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class Td1AppServiceTest {

	@InjectMocks
	Td1AppService _appService;

	@Mock
	private Td1Manager _td1Manager;
	
	@Mock
	private Td1Mapper _mapper;

	@Mock
	private Logger loggerMock;

	@Mock
	private LoggingHelper logHelper;
	

	private static Long ID=15L;
	
	@Before
	public void setUp() throws Exception {

		MockitoAnnotations.initMocks(_appService);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void findTd1ById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

		Mockito.when(_td1Manager.FindById(anyLong())).thenReturn(null);
		Assertions.assertThat(_appService.FindById(ID)).isEqualTo(null);
	}
	
	@Test
	public void findTd1ById_IdIsNotNullAndIdExists_ReturnTd1() {

		Td1Entity td1 = mock(Td1Entity.class);
		Mockito.when(_td1Manager.FindById(anyLong())).thenReturn(td1);
		Assertions.assertThat(_appService.FindById(ID)).isEqualTo(_mapper.Td1EntityToFindTd1ByIdOutput(td1));
	}
	
	 @Test 
    public void createTd1_Td1IsNotNullAndTd1DoesNotExist_StoreTd1() { 
 
       Td1Entity td1Entity = mock(Td1Entity.class); 
       CreateTd1Input td1 = new CreateTd1Input();
   
        
       Mockito.when(_mapper.CreateTd1InputToTd1Entity(any(CreateTd1Input.class))).thenReturn(td1Entity); 
       Mockito.when(_td1Manager.Create(any(Td1Entity.class))).thenReturn(td1Entity);
      
       Assertions.assertThat(_appService.Create(td1)).isEqualTo(_mapper.Td1EntityToCreateTd1Output(td1Entity)); 
    } 
	@Test
	public void updateTd1_Td1IdIsNotNullAndIdExists_ReturnUpdatedTd1() {

		Td1Entity td1Entity = mock(Td1Entity.class);
		UpdateTd1Input td1= mock(UpdateTd1Input.class);
		Mockito.when(_mapper.UpdateTd1InputToTd1Entity(any(UpdateTd1Input.class))).thenReturn(td1Entity);
		Mockito.when(_td1Manager.Update(any(Td1Entity.class))).thenReturn(td1Entity);
		Assertions.assertThat(_appService.Update(ID,td1)).isEqualTo(_mapper.Td1EntityToUpdateTd1Output(td1Entity));
	}
    
	@Test
	public void deleteTd1_Td1IsNotNullAndTd1Exists_Td1Removed() {

		Td1Entity td1= mock(Td1Entity.class);
		Mockito.when(_td1Manager.FindById(anyLong())).thenReturn(td1);
		_appService.Delete(ID); 
		verify(_td1Manager).Delete(td1);
	}
	
	@Test
	public void Find_ListIsEmpty_ReturnList() throws Exception {

		List<Td1Entity> list = new ArrayList<>();
		Page<Td1Entity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindTd1ByIdOutput> output = new ArrayList<>();
		SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");

		Mockito.when(_td1Manager.FindAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.Find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void Find_ListIsNotEmpty_ReturnList() throws Exception {

		List<Td1Entity> list = new ArrayList<>();
		Td1Entity td1 = mock(Td1Entity.class);
		list.add(td1);
    	Page<Td1Entity> foundPage = new PageImpl(list);
		Pageable pageable = mock(Pageable.class);
		List<FindTd1ByIdOutput> output = new ArrayList<>();
        SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");
		output.add(_mapper.Td1EntityToFindTd1ByIdOutput(td1));
    	Mockito.when(_td1Manager.FindAll(any(Predicate.class),any(Pageable.class))).thenReturn(foundPage);
		Assertions.assertThat(_appService.Find(search, pageable)).isEqualTo(output);
	}
	
	@Test
	public void searchAllProperties_SearchIsNotNull_ReturnBooleanBuilder() {
		String search= "xyz";
		String operator= "equals";
		QTd1Entity td1 = QTd1Entity.td1Entity;
		BooleanBuilder builder = new BooleanBuilder();
		Assertions.assertThat(_appService.searchAllProperties(td1,search,operator)).isEqualTo(builder);
	}
	
	@Test
	public void searchSpecificProperty_PropertyExists_ReturnBooleanBuilder() {
		String operator= "equals";
		List<String> list = new ArrayList<>();
		
		QTd1Entity td1 = QTd1Entity.td1Entity;
		BooleanBuilder builder = new BooleanBuilder();
		
		Assertions.assertThat(_appService.searchSpecificProperty(td1, list,"xyz",operator)).isEqualTo(builder);
	}
	
	@Test
	public void searchKeyValuePair_PropertyExists_ReturnBooleanBuilder() {
		QTd1Entity td1 = QTd1Entity.td1Entity;
	    SearchFields searchFields = new SearchFields();
		searchFields.setOperator("equals");
		searchFields.setSearchValue("xyz");
	    Map<String,SearchFields> map = new HashMap<>();
		 Map<String,String> searchMap = new HashMap<>();
        searchMap.put("xyz",String.valueOf(ID));
		BooleanBuilder builder = new BooleanBuilder();
		Assertions.assertThat(_appService.searchKeyValuePair(td1,map,searchMap)).isEqualTo(builder);
	}
	
	@Test (expected = Exception.class)
	public void checkProperties_PropertyDoesNotExist_ThrowException() throws Exception {
		List<String> list = new ArrayList<>();
		list.add("xyz");
		_appService.checkProperties(list);
	}
	
	@Test
	public void checkProperties_PropertyExists_ReturnNothing() throws Exception {
		List<String> list = new ArrayList<>();
		_appService.checkProperties(list);
	}
	
	@Test
	public void search_SearchIsNotNullAndSearchContainsCaseOne_ReturnBooleanBuilder() throws Exception {

		QTd1Entity td1 = QTd1Entity.td1Entity;
		SearchCriteria search= new SearchCriteria();
		search.setType(1);
		search.setValue("xyz");
		search.setOperator("equals");
		BooleanBuilder builder = new BooleanBuilder();
		Assertions.assertThat(_appService.Search(search)).isEqualTo(builder);
	}
	
	@Test
	public void  search_SearchIsNotNullAndSearchContainsCaseTwo_ReturnBooleanBuilder() throws Exception {

		QTd1Entity td1 = QTd1Entity.td1Entity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(2);
		search.setValue("xyz");
		search.setOperator("equals");
        fieldsList.add(fields);
        search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
		Assertions.assertThat(_appService.Search(search)).isEqualTo(builder);
	}
	
	@Test
	public void  search_SearchIsNotNullAndSearchContainsCaseThree_ReturnBooleanBuilder() throws Exception {
	
		Map<String,SearchFields> map = new HashMap<>();
		QTd1Entity td1 = QTd1Entity.td1Entity;
		List<SearchFields> fieldsList= new ArrayList<>();
		SearchFields fields=new SearchFields();
		SearchCriteria search= new SearchCriteria();
		search.setType(3);
		search.setValue("xyz");
		search.setOperator("equals");
        fields.setOperator("equals");
		fields.setSearchValue("xyz");
        fieldsList.add(fields);
        search.setFields(fieldsList);
		BooleanBuilder builder = new BooleanBuilder();
		Assertions.assertThat(_appService.Search(search)).isEqualTo(builder);
	}
	
	@Test
	public void  search_StringIsNull_ReturnNull() throws Exception {

		Assertions.assertThat(_appService.Search(null)).isEqualTo(null);
	}
	

}

