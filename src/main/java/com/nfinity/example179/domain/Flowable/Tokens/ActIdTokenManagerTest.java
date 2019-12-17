package com.nfinity.example179.domain.Flowable.Tokens;

import com.nfinity.example179.domain.IRepository.IActIdTokenRepository;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;
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
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

	@Component
	@RunWith(SpringJUnit4ClassRunner.class)
	public class ActIdTokenManagerTest {

	@InjectMocks
	ActIdTokenManager tokenManager;

	@Mock
	IActIdTokenRepository _tokenRepository;

	@Mock
	private Logger loggerMock;

	@Mock
	private LoggingHelper logHelper;

	private static long ID=15;

	@Before
	public void setUp() throws Exception {
	MockitoAnnotations.initMocks(tokenManager);
	when(logHelper.getLogger()).thenReturn(loggerMock);
	doNothing().when(loggerMock).error(anyString());
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void generateNewToken_IdIsNotNull_ReturnAToken() {
	ActIdTokenEntity tokenEntity = new ActIdTokenEntity();
	tokenEntity.setId("TokenId1");
	tokenEntity.setRev(0L);
	Assertions.assertThat(tokenManager.generateNewToken("TokenId1")).isEqualTo(tokenEntity);
	}
	
	@Test
	public void insertToken_UserIsNotNullAndUserDoesNotExist_StoreAToken() {

	ActIdTokenEntity tokenEntity = mock(ActIdTokenEntity.class);
	tokenManager.insertToken(tokenEntity);
	verify(_tokenRepository).save(tokenEntity);
	}

	@Test
	public void updateToken_TokenIsNotNullAndTokenExists_UpdateAToken() {

	ActIdTokenEntity tokenEntity = mock(ActIdTokenEntity.class);
	tokenManager.updateToken(tokenEntity);
	verify(_tokenRepository).save(tokenEntity);
	}

	@Test
	public void isNewToken_TokenIsNotNull_ReturnAToken() {
	ActIdTokenEntity tokenEntity = mock(ActIdTokenEntity.class);
	Assertions.assertThat(tokenManager.isNewToken(tokenEntity)).isEqualTo(true);
	}
}
