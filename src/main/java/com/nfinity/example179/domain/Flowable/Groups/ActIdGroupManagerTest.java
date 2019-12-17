package com.nfinity.example179.domain.Flowable.Groups;

import com.nfinity.example179.CommonModule.logging.LoggingHelper;
import com.nfinity.example179.domain.IRepository.IActIdGroupRepository;
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
public class ActIdGroupManagerTest {
@InjectMocks
ActIdGroupManager groupManager;

@Mock
IActIdGroupRepository _groupRepository;

@Mock
private Logger loggerMock;

@Mock
private LoggingHelper logHelper;

private static long ID=15;

@Before
public void setUp() throws Exception {
MockitoAnnotations.initMocks(groupManager);
when(logHelper.getLogger()).thenReturn(loggerMock);
doNothing().when(loggerMock).error(anyString());
}

@After
public void tearDown() throws Exception {
}
@Test
public void findByGroupId_IdIsNotNullAndIdExists_ReturnAGroup() {
ActIdGroupEntity groupEntity = mock(ActIdGroupEntity.class);
Mockito.when(_groupRepository.findByGroupId(anyString())).thenReturn(groupEntity);
Assertions.assertThat(groupManager.findByGroupId("Group1")).isEqualTo(groupEntity);
}

@Test
public void findByGroupId_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
Mockito.when(_groupRepository.findByGroupId(anyString())).thenReturn(null);
Assertions.assertThat(groupManager.findByGroupId("InvalidGroup")).isEqualTo(null);
}
@Test
public void createGroup_GroupIsNotNullAndGroupDoesNotExist_StoreAGroup() {

ActIdGroupEntity groupEntity = mock(ActIdGroupEntity.class);
groupManager.create(groupEntity);
verify(_groupRepository).save(groupEntity);
}

@Test
public void deleteGroup_GroupExists_RemoveAGroup() {

ActIdGroupEntity groupEntity = mock(ActIdGroupEntity.class);
groupManager.delete(groupEntity);
verify(_groupRepository).delete(groupEntity);
}
}
