package com.nfinity.example179.EmailBuilder.domain.EmailVariable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.nfinity.example179.EmailBuilder.domain.IRepository.IEmailVariableRepository;
import com.nfinity.example179.EmailBuilder.domain.model.EmailVariableEntity;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class EmailVariableManagerTest {

	@InjectMocks
	IEmailVariableManager emailVariableManager;
	
	@Mock
	IEmailVariableRepository  _emailVariableRepository;
	
	@Mock
    private Logger loggerMock;
   
	@Mock
	private LoggingHelper logHelper;
	
	private static long ID=15;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(emailVariableManager);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void findEmailVariableById_IdIsNotNullAndIdExists_ReturnEmailVariable() {
		EmailVariableEntity email =mock(EmailVariableEntity.class);

		Mockito.when(_emailVariableRepository.findById(anyLong())).thenReturn(email);
		Assertions.assertThat(emailVariableManager.FindById(ID)).isEqualTo(email);
	}

	@Test 
	public void findEmailVariableById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

	Mockito.when(_emailVariableRepository.findById(anyLong())).thenReturn(null);
	Assertions.assertThat(emailVariableManager.FindById(ID)).isEqualTo(null);
	}
	
	@Test
	public void findEmailVariableByName_NameIsNotNullAndNameExists_ReturnEmailVariable() {
		EmailVariableEntity email =mock(EmailVariableEntity.class);

		Mockito.when(_emailVariableRepository.findByEmailName(anyString())).thenReturn(email);
		Assertions.assertThat(emailVariableManager.FindByName("email1")).isEqualTo(email);
	}

	@Test 
	public void findEmailVariableByName_NameIsNotNullAndNameDoesNotExist_ReturnNull() {

		Mockito.when(_emailVariableRepository.findByEmailName(anyString())).thenReturn(null);
		Assertions.assertThat(emailVariableManager.FindByName("email1")).isEqualTo(null);
	
	}
	
	@Test
	public void createEmailVariable_EmailVariableIsNotNullAndEmailVariableDoesNotExist_StoreEmailVariable() {

		EmailVariableEntity email =mock(EmailVariableEntity.class);
		Mockito.when(_emailVariableRepository.save(any(EmailVariableEntity.class))).thenReturn(email);
		Assertions.assertThat(emailVariableManager.Create(email)).isEqualTo(email);
	}

	@Test
	public void deleteEmailVariable_EmailVariableExists_RemoveEmailVariable() {

		EmailVariableEntity email =mock(EmailVariableEntity.class);
		emailVariableManager.Delete(email);
		verify(_emailVariableRepository).delete(email);
	}

	@Test
	public void updateEmailVariable_EmailVariableIsNotNullAndEmailVariableExists_UpdateEmailVariable() {
		
		EmailVariableEntity email =mock(EmailVariableEntity.class);
		Mockito.when(_emailVariableRepository.save(any(EmailVariableEntity.class))).thenReturn(email);
		Assertions.assertThat(emailVariableManager.Update(email)).isEqualTo(email);
		
	}


	@Test
	public void findAll_PageableIsNotNull_ReturnPage() {
		Page<EmailVariableEntity> email = mock(Page.class);
		Pageable pageable = mock(Pageable.class);
		Predicate predicate = mock(Predicate.class);

		Mockito.when(_emailVariableRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(email);
		Assertions.assertThat(emailVariableManager.FindAll(predicate,pageable)).isEqualTo(email);
	}
}