package com.nfinity.example179.EmailBuilder.domain.EmailTemplate;

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

import com.nfinity.example179.EmailBuilder.domain.IRepository.IEmailTemplateRepository;
import com.nfinity.example179.EmailBuilder.domain.model.EmailTemplateEntity;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;
import com.querydsl.core.types.Predicate;

@RunWith(SpringJUnit4ClassRunner.class)
public class EmailTemplateManagerTest {

	@InjectMocks
	IEmailTemplateManager emailTemplateManager;
	
	@Mock
	IEmailTemplateRepository  _emailTemplateRepository;
	
	@Mock
    private Logger loggerMock;
   
	@Mock
	private LoggingHelper logHelper;
	
	private static long ID=15;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(emailTemplateManager);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void findEmailById_IdIsNotNullAndIdExists_ReturnEmail() {
		EmailTemplateEntity email =mock(EmailTemplateEntity.class);

		Mockito.when(_emailTemplateRepository.findById(anyLong())).thenReturn(email);
		Assertions.assertThat(emailTemplateManager.FindById(ID)).isEqualTo(email);
	}

	@Test 
	public void findEmailById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {

	Mockito.when(_emailTemplateRepository.findById(anyLong())).thenReturn(null);
	Assertions.assertThat(emailTemplateManager.FindById(ID)).isEqualTo(null);
	}
	
	@Test
	public void findEmailByName_NameIsNotNullAndNameExists_ReturnEmail() {
		EmailTemplateEntity email =mock(EmailTemplateEntity.class);

		Mockito.when(_emailTemplateRepository.findByEmailName(anyString())).thenReturn(email);
		Assertions.assertThat(emailTemplateManager.FindByName("xyz")).isEqualTo(email);
	}

	@Test 
	public void findEmailByName_NameIsNotNullAndNameDoesNotExist_ReturnNull() {

		Mockito.when(_emailTemplateRepository.findByEmailName(anyString())).thenReturn(null);
		Assertions.assertThat(emailTemplateManager.FindByName("xyz")).isEqualTo(null);
	
	}
	
	@Test
	public void createEmail_EmailIsNotNullAndEmailDoesNotExist_StoreEmail() {

		EmailTemplateEntity email =mock(EmailTemplateEntity.class);
		Mockito.when(_emailTemplateRepository.save(any(EmailTemplateEntity.class))).thenReturn(email);
		Assertions.assertThat(emailTemplateManager.Create(email)).isEqualTo(email);
	}

	@Test
	public void deleteEmail_EmailExists_RemoveEmail() {

		EmailTemplateEntity email =mock(EmailTemplateEntity.class);
		emailTemplateManager.Delete(email);
		verify(_emailTemplateRepository).delete(email);
	}

	@Test
	public void updateEmail_EmailIsNotNullAndEmailExists_UpdateEmail() {
		
		EmailTemplateEntity email =mock(EmailTemplateEntity.class);
		Mockito.when(_emailTemplateRepository.save(any(EmailTemplateEntity.class))).thenReturn(email);
		Assertions.assertThat(emailTemplateManager.Update(email)).isEqualTo(email);
		
	}

	@Test
	public void findAll_PageableIsNotNull_ReturnPage() {
		Page<EmailTemplateEntity> email = mock(Page.class);
		Pageable pageable = mock(Pageable.class);
		Predicate predicate = mock(Predicate.class);

		Mockito.when(_emailTemplateRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(email);
		Assertions.assertThat(emailTemplateManager.FindAll(predicate,pageable)).isEqualTo(email);
	}
}