package com.nfinity.example179.domain.Flowable.Privileges;

import com.nfinity.example179.domain.IRepository.IActIdPrivRepository;
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
public class ActIdPrivManagerTest {
@InjectMocks
ActIdPrivManager privManager;

@Mock
IActIdPrivRepository _privRepository;

@Mock
private Logger loggerMock;

@Mock
private LoggingHelper logHelper;

private static long ID=15;

@Before
public void setUp() throws Exception {
MockitoAnnotations.initMocks(privManager);
when(logHelper.getLogger()).thenReturn(loggerMock);
doNothing().when(loggerMock).error(anyString());
}

@After
public void tearDown() throws Exception {
}

@Test
public void findByName_NameIsNotNullAndNameExists_ReturnAPriv() {
ActIdPrivEntity privEntity = mock(ActIdPrivEntity.class);

Mockito.when(_privRepository.findByName(anyString())).thenReturn(privEntity);
Assertions.assertThat(privManager.findByName("Priv1")).isEqualTo(privEntity);
}

@Test
public void findByName_NameIsNotNullAndNameDoesNotExist_ReturnNull() {
Mockito.when(_privRepository.findByName(anyString())).thenReturn(null);
Assertions.assertThat(privManager.findByName("Priv1")).isEqualTo(null);
}
@Test
public void createPriv_PrivIsNotNullAndPrivDoesNotExist_StoreAPriv() {

ActIdPrivEntity privEntity = mock(ActIdPrivEntity.class);
privManager.create(privEntity);
verify(_privRepository).save(privEntity);
}

@Test
public void deletePriv_PrivExists_RemoveAPriv() {

ActIdPrivEntity privEntity = mock(ActIdPrivEntity.class);
privManager.delete(privEntity);
verify(_privRepository).delete(privEntity);
}

@Test
public void updatePriv_PrivIsNotNullAndPrivExists_UpdateAPriv() {

ActIdPrivEntity privEntity = mock(ActIdPrivEntity.class);
privManager.update(privEntity);
verify(_privRepository).save(privEntity);
}
}
