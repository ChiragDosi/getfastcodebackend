package com.nfinity.example179.domain.Td1;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

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
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nfinity.example179.domain.model.Td1Entity;
import com.nfinity.example179.domain.IRepository.ITd1Repository;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class Td1ManagerTest {

	@InjectMocks
	Td1Manager _td1Manager;
	
	@Mock
	ITd1Repository  _td1Repository;
	@Mock
    private Logger loggerMock;
   
	@Mock
	private LoggingHelper logHelper;
	
	private static Long ID=15L;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(_td1Manager);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void findTd1ById_IdIsNotNullAndIdExists_ReturnTd1() {
		Td1Entity td1 =mock(Td1Entity.class);

        Optional<Td1Entity> dbTd1 = Optional.of((Td1Entity) td1);
		Mockito.<Optional<Td1Entity>>when(_td1Repository.findById(anyLong())).thenReturn(dbTd1);
		Assertions.assertThat(_td1Manager.FindById(ID)).isEqualTo(td1);
	}

	@Test 
	public void findTd1ById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

	    Mockito.<Optional<Td1Entity>>when(_td1Repository.findById(anyLong())).thenReturn(Optional.empty());
		Assertions.assertThat(_td1Manager.FindById(ID)).isEqualTo(null);
	}
	
	@Test
	public void createTd1_Td1IsNotNullAndTd1DoesNotExist_StoreTd1() {

		Td1Entity td1 =mock(Td1Entity.class);
		Mockito.when(_td1Repository.save(any(Td1Entity.class))).thenReturn(td1);
		Assertions.assertThat(_td1Manager.Create(td1)).isEqualTo(td1);
	}

	@Test
	public void deleteTd1_Td1Exists_RemoveTd1() {

		Td1Entity td1 =mock(Td1Entity.class);
		_td1Manager.Delete(td1);
		verify(_td1Repository).delete(td1);
	}

	@Test
	public void updateTd1_Td1IsNotNullAndTd1Exists_UpdateTd1() {
		
		Td1Entity td1 =mock(Td1Entity.class);
		Mockito.when(_td1Repository.save(any(Td1Entity.class))).thenReturn(td1);
		Assertions.assertThat(_td1Manager.Update(td1)).isEqualTo(td1);
		
	}

	@Test
	public void findAll_PageableIsNotNull_ReturnPage() {
		Page<Td1Entity> td1 = mock(Page.class);
		Pageable pageable = mock(Pageable.class);
		Predicate predicate = mock(Predicate.class);

		Mockito.when(_td1Repository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(td1);
		Assertions.assertThat(_td1Manager.FindAll(predicate,pageable)).isEqualTo(td1);
	}
	
}
