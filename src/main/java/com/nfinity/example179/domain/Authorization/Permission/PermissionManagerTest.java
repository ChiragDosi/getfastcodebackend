package com.nfinity.example179.domain.Authorization.Permission;

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
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.nfinity.example179.domain.IRepository.IPermissionRepository;
import com.nfinity.example179.domain.model.PermissionEntity;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;
import com.querydsl.core.types.Predicate;

@Component
@RunWith(SpringJUnit4ClassRunner.class)
public class PermissionManagerTest {

	@InjectMocks
	PermissionManager permissionManager;

	@Mock
	private IPermissionRepository _permissionRepository;

	@Mock
    private Logger loggerMock;
   
	@Mock
	private LoggingHelper logHelper;
	
	private static long ID=15;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(permissionManager);
		when(logHelper.getLogger()).thenReturn(loggerMock);
		doNothing().when(loggerMock).error(anyString());

	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void findPermissionById_IdIsNotNullAndIdExists_ReturnAPermission() {
		PermissionEntity permission =mock(PermissionEntity.class);
		
		Optional<PermissionEntity> dbPermission = Optional.of((PermissionEntity) permission);
		Mockito.<Optional<PermissionEntity>>when(_permissionRepository.findById(anyLong())).thenReturn(dbPermission);
		Assertions.assertThat(permissionManager.FindById(ID)).isEqualTo(permission);
	}
 
	@Test 
	public void findPermissionById_IdIsNotNullAndIdDoesNotExist_ReturnNull() {
	 
	 Mockito.<Optional<PermissionEntity>>when(_permissionRepository.findById(anyLong())).thenReturn(Optional.empty());
	 Assertions.assertThat(permissionManager.FindById(ID)).isEqualTo(null);
	}
	
	@Test
	public void findPermissionByName_NameIsNotNullAndNameExists_ReturnAPermission() {
		PermissionEntity permission =mock(PermissionEntity.class);

		Mockito.when(_permissionRepository.findByPermissionName(anyString())).thenReturn(permission);
		Assertions.assertThat(permissionManager.FindByPermissionName("permission1")).isEqualTo(permission);
	}

	@Test 
	public void findPermissionByName_NameIsNotNullAndNameDoesNotExist_ReturnNull() {

		Mockito.when(_permissionRepository.findByPermissionName(anyString())).thenReturn(null);
		Assertions.assertThat(permissionManager.FindByPermissionName("permission1")).isEqualTo(null);
	
	}
	
	@Test
	public void createPermission_PermissionIsNotNullAndPermissionDoesNotExist_StoreAPermission() {

		PermissionEntity permission =mock(PermissionEntity.class);
		Mockito.when(_permissionRepository.save(any(PermissionEntity.class))).thenReturn(permission);
		Assertions.assertThat(permissionManager.Create(permission)).isEqualTo(permission);
	}

	@Test
	public void deletePermission_PermissionExists_RemoveAPermission() {

		PermissionEntity permission =mock(PermissionEntity.class);
		permissionManager.Delete(permission);
		verify(_permissionRepository).delete(permission);
	}

	@Test
	public void updatePermission_PermissionIsNotNullAndPermissionExists_UpdateAPermission() {
		
		PermissionEntity permission =mock(PermissionEntity.class);
		Mockito.when(_permissionRepository.save(any(PermissionEntity.class))).thenReturn(permission);
		Assertions.assertThat(permissionManager.Update(permission)).isEqualTo(permission);
		
	}

	@Test
	public void findAll_PageableIsNotNull_ReturnPage() {
		Page<PermissionEntity> permission = mock(Page.class);
		Pageable pageable = mock(Pageable.class);
		Predicate predicate = mock(Predicate.class);

		Mockito.when(_permissionRepository.findAll(any(Predicate.class),any(Pageable.class))).thenReturn(permission);
		Assertions.assertThat(permissionManager.FindAll(predicate,pageable)).isEqualTo(permission);
	}
	
}