package com.nfinity.example179.domain.Flowable.Users;

import com.nfinity.example179.domain.IRepository.IActIdUserRepository;
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
public class ActIdUserManagerTest {
@InjectMocks
ActIdUserManager userManager;

@Mock
IActIdUserRepository _userRepository;

@Mock
private Logger loggerMock;

@Mock
private LoggingHelper logHelper;

private static long ID=15;

@Before
public void setUp() throws Exception {
MockitoAnnotations.initMocks(userManager);
when(logHelper.getLogger()).thenReturn(loggerMock);
doNothing().when(loggerMock).error(anyString());
}

@After
public void tearDown() throws Exception {
}

@Test
public void findByUserId_IdIsNotNullAndIdExists_ReturnAUser() {
ActIdUserEntity userEntity = mock(ActIdUserEntity.class);
Mockito.when(_userRepository.findByUserId(anyString())).thenReturn(userEntity);
Assertions.assertThat(userManager.findByUserId("User1")).isEqualTo(userEntity);
}

@Test
public void findByUserId_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
Mockito.when(_userRepository.findByUserId(anyString())).thenReturn(null);
Assertions.assertThat(userManager.findByUserId("InvalidUser")).isEqualTo(null);
}

@Test
public void findByUserEmail_EmailIsNotNullAndEmailExists_ReturnAUser() {
ActIdUserEntity userEntity = mock(ActIdUserEntity.class);

Mockito.when(_userRepository.findByUserEmail(anyString())).thenReturn(userEntity);
Assertions.assertThat(userManager.findByUserEmail("User1")).isEqualTo(userEntity);
}

@Test
public void findByUserEmail_EmailIsNotNullAndEmailDoesNotExist_ReturnNull() {
Mockito.when(_userRepository.findByUserEmail(anyString())).thenReturn(null);
Assertions.assertThat(userManager.findByUserEmail("User1")).isEqualTo(null);
}
@Test
public void createUser_UserIsNotNullAndUserDoesNotExist_StoreAUser() {

ActIdUserEntity userEntity = mock(ActIdUserEntity.class);
userManager.create(userEntity);
verify(_userRepository).save(userEntity);
}

@Test
public void deleteUser_UserExists_RemoveAUser() {

ActIdUserEntity userEntity = mock(ActIdUserEntity.class);
userManager.delete(userEntity);
verify(_userRepository).delete(userEntity);
}

@Test
public void updateUser_UserIsNotNullAndUserExists_UpdateAUser() {

ActIdUserEntity userEntity = mock(ActIdUserEntity.class);
userManager.update(userEntity);
verify(_userRepository).save(userEntity);
}
}
