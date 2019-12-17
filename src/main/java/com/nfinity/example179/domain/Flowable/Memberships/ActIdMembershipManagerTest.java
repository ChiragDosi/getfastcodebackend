package com.nfinity.example179.domain.Flowable.Memberships;

import com.nfinity.example179.domain.IRepository.IActIdMembershipRepository;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Component
@RunWith(SpringJUnit4ClassRunner.class)
public class ActIdMembershipManagerTest {
@InjectMocks
ActIdMembershipManager membershipManager;

@Mock
IActIdMembershipRepository _membershipRepository;

@Mock
private Logger loggerMock;

@Mock
private LoggingHelper logHelper;

private static long ID=15;

@Before
public void setUp() throws Exception {
MockitoAnnotations.initMocks(membershipManager);
when(logHelper.getLogger()).thenReturn(loggerMock);
doNothing().when(loggerMock).error(anyString());
}

@After
public void tearDown() throws Exception {
}


@Test
public void findByUserGroupId_IdIsNotNullAndIdExists_ReturnAMembership() {
ActIdMembershipEntity membershipEntity = mock(ActIdMembershipEntity.class);
Mockito.when(_membershipRepository.findByUserGroupId(anyString(), anyString())).thenReturn(membershipEntity);
Assertions.assertThat(membershipManager.findByUserGroupId("User1", "Group1")).isEqualTo(membershipEntity);
}

@Test
public void findByUserGroupId_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
Mockito.when(_membershipRepository.findByUserGroupId(anyString(), anyString())).thenReturn(null);
Assertions.assertThat(membershipManager.findByUserGroupId("InvalidUser", "InvalidGroup")).isEqualTo(null);
}

@Test
public void findAllByUserId_IdIsNotNullAndIdExists_ReturnAllMemberships() {
List<ActIdMembershipEntity> membershipEntity = mock(List.class);

    Mockito.when(_membershipRepository.findAllByUserId(anyString())).thenReturn(membershipEntity);
    Assertions.assertThat(membershipManager.findAllByUserId("User1")).isEqualTo(membershipEntity);
    }

    @Test
    public void findAllByUserId_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
    Mockito.when(_membershipRepository.findAllByUserId(anyString())).thenReturn(null);
    Assertions.assertThat(membershipManager.findAllByUserId("User1")).isEqualTo(null);
    }
    @Test
    public void findAllByGroupId_IdIsNotNullAndIdExists_ReturnAllMemberships() {
    List<ActIdMembershipEntity> membershipEntity = mock(List.class);

        Mockito.when(_membershipRepository.findAllByGroupId(anyString())).thenReturn(membershipEntity);
        Assertions.assertThat(membershipManager.findAllByGroupId("Group1")).isEqualTo(membershipEntity);
        }

        @Test
        public void findAllByGroupId_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
        Mockito.when(_membershipRepository.findAllByGroupId(anyString())).thenReturn(null);
        Assertions.assertThat(membershipManager.findAllByGroupId("Group1")).isEqualTo(null);
        }
        @Test
        public void createMembership_MembershipIsNotNullAndMembershipDoesNotExist_StoreAMembership() {

        ActIdMembershipEntity membershipEntity = mock(ActIdMembershipEntity.class);
        membershipManager.create(membershipEntity);
        verify(_membershipRepository).save(membershipEntity);
        }

        @Test
        public void deleteMembership_MembershipExists_RemoveAMembership() {

        ActIdMembershipEntity membershipEntity = mock(ActIdMembershipEntity.class);
        membershipManager.delete(membershipEntity);
        verify(_membershipRepository).delete(membershipEntity);
        }
        }
