package com.nfinity.example179.domain.Flowable.PrivilegeMappings;

import com.nfinity.example179.domain.Flowable.Privileges.ActIdPrivEntity;
import com.nfinity.example179.domain.Flowable.Users.ActIdUserEntity;
import com.nfinity.example179.domain.IRepository.IActIdPrivMappingRepository;
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

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

@Component
@RunWith(SpringJUnit4ClassRunner.class)
public class ActIdPrivMappingManagerTest {
@InjectMocks
ActIdPrivMappingManager privMappingManager;

@Mock
IActIdPrivMappingRepository _privMappingRepository;

@Mock
private Logger loggerMock;

@Mock
private LoggingHelper logHelper;

private static long ID=15;

@Before
public void setUp() throws Exception {
MockitoAnnotations.initMocks(privMappingManager);
when(logHelper.getLogger()).thenReturn(loggerMock);
doNothing().when(loggerMock).error(anyString());

}

@After
public void tearDown() throws Exception {
}

@Test
public void createUserPrivilege_ifUserAndPrivilegeIsNotNull_PrivilegeGranted() {
ActIdPrivMappingEntity privMapping = mock(ActIdPrivMappingEntity.class);

privMappingManager.create(privMapping);
verify(_privMappingRepository).save(privMapping);
}
@Test
public void updateUserPrivilege_ifUserAndPrivilegeIsNotNull_PrivilegeGranted() {
ActIdPrivMappingEntity privMapping = mock(ActIdPrivMappingEntity.class);

privMappingManager.update(privMapping);
verify(_privMappingRepository).save(privMapping);
}
@Test
public void RemoveUserPrivilege_ifUserAndPrivilegeIsNotNull_PrivilegeRemoved() {
ActIdPrivMappingEntity privMapping = mock(ActIdPrivMappingEntity.class);

privMappingManager.delete(privMapping);
verify(_privMappingRepository).delete(privMapping);
}

@Test
public void createGroupPrivilege_ifGroupAndPrivilegeIsNotNull_PrivilegeGranted() {
ActIdPrivMappingEntity privMapping = mock(ActIdPrivMappingEntity.class);

privMappingManager.create(privMapping);
verify(_privMappingRepository).save(privMapping);
}
@Test
public void updateGroupPrivilege_ifGroupAndPrivilegeIsNotNull_PrivilegeGranted() {
ActIdPrivMappingEntity privMapping = mock(ActIdPrivMappingEntity.class);

privMappingManager.update(privMapping);
verify(_privMappingRepository).save(privMapping);
}
@Test
public void RemoveGroupPrivilege_ifGroupAndPrivilegeIsNotNull_PrivilegeRemoved() {
ActIdPrivMappingEntity privMapping = mock(ActIdPrivMappingEntity.class);

privMappingManager.delete(privMapping);
verify(_privMappingRepository).delete(privMapping);
}
@Test
public void findByUserPrivilege_UserPrivilegeIsNotNullAndExists_ReturnAPrivilegeMapping() {
ActIdPrivMappingEntity privMapping = mock(ActIdPrivMappingEntity.class);
Mockito.when(_privMappingRepository.findByUserPrivilege(anyString(), anyString())).thenReturn(privMapping);
Assertions.assertThat(privMappingManager.findByUserPrivilege("User1", "ROLE_CREATE")).isEqualTo(privMapping);
}

@Test
public void findByUserPrivilege_UserPrivilegeIsNotNullAndDoesNotExists_ReturnNull() {
Mockito.when(_privMappingRepository.findByUserPrivilege(anyString(), anyString())).thenReturn(null);
Assertions.assertThat(privMappingManager.findByUserPrivilege("InvalidUser", "InvalidRole")).isEqualTo(null);
}
@Test
public void findByGroupPrivilege_GroupPrivilegeIsNotNullAndExists_ReturnAPrivilegeMapping() {
ActIdPrivMappingEntity privMapping = mock(ActIdPrivMappingEntity.class);
Mockito.when(_privMappingRepository.findByGroupPrivilege(anyString(), anyString())).thenReturn(privMapping);
Assertions.assertThat(privMappingManager.findByGroupPrivilege("User1", "ROLESENTITY_CREATE")).isEqualTo(privMapping);
}

@Test
public void findByGroupPrivilege_GroupPrivilegeIsNotNullAndDoesNotExists_ReturnNull() {
Mockito.when(_privMappingRepository.findByGroupPrivilege(anyString(), anyString())).thenReturn(null);
Assertions.assertThat(privMappingManager.findByGroupPrivilege("InvalidUser", "InvalidRole")).isEqualTo(null);
}
@Test
public void findAllByUserId_UserIsNotNullAndExists_ReturnAllPrivilegeMapping() {
List<ActIdPrivMappingEntity> privMapping = mock(List.class);
    Mockito.when(_privMappingRepository.findAllByUserId(anyString())).thenReturn(privMapping);
    Assertions.assertThat(privMappingManager.findAllByUserId("User1")).isEqualTo(privMapping);
    }

    @Test
    public void findAllByUserId_UserIsNotNullAndDoesNotExists_ReturnNull() {
    Mockito.when(_privMappingRepository.findAllByUserId(anyString())).thenReturn(null);
    Assertions.assertThat(privMappingManager.findAllByUserId("InvalidUser")).isEqualTo(null);
    }
    @Test
    public void findAllByGroupId_GroupIsNotNullAndExists_ReturnAPrivilegeMapping() {
    List<ActIdPrivMappingEntity> privMapping = mock(List.class);
        Mockito.when(_privMappingRepository.findAllByGroupId(anyString())).thenReturn(privMapping);
        Assertions.assertThat(privMappingManager.findAllByGroupId("ROLESENTITY_CREATE")).isEqualTo(privMapping);
        }

        @Test
        public void findAllByGroupId_GroupIsNotNullAndDoesNotExists_ReturnNull() {
        Mockito.when(_privMappingRepository.findAllByGroupId(anyString())).thenReturn(null);
        Assertions.assertThat(privMappingManager.findAllByGroupId("InvalidRole")).isEqualTo(null);
        }
        }
