package com.nfinity.example179;

import com.nfinity.example179.application.Authorization.User.IUserAppService;
import com.nfinity.example179.domain.model.RolepermissionEntity;
import com.nfinity.example179.domain.model.UserpermissionEntity;
import com.nfinity.example179.domain.model.UserroleEntity;
import com.nfinity.example179.domain.model.UserEntity;
import com.nfinity.example179.domain.IRepository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	public UserDetailsServiceImpl(IUserAppService userAppService) {
	}

	@Autowired
	private IUserRepository usersRepository;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

     
		UserEntity applicationUser = usersRepository.findByUserName(username);

		if (applicationUser == null) {
			throw new UsernameNotFoundException(username);
		}

		List<String> permissions = getAllPermissions(applicationUser);
		List<GrantedAuthority> authorities = getGrantedAuthorities(permissions);

		return new User(applicationUser.getUserName(), applicationUser.getPassword(), authorities); // User class implements UserDetails Interface
	
	}


	private List<String> getAllPermissions(UserEntity user) {

		List<String> permissions = new ArrayList<>();
        Set<UserroleEntity> ure = user.getUserroleSet();
        Iterator rIterator = ure.iterator();
		while (rIterator.hasNext()) {
            UserroleEntity re = (UserroleEntity) rIterator.next();
            Set<RolepermissionEntity> srp= re.getRole().getRolepermissionSet();
            for (RolepermissionEntity item : srp) {
				permissions.add(item.getPermission().getName());
            }
		}
		
		Set<UserpermissionEntity> spe = user.getUserpermissionSet();
        Iterator pIterator = spe.iterator();
		while (pIterator.hasNext()) {
            UserpermissionEntity pe = (UserpermissionEntity) pIterator.next();
            
            if(permissions.contains(pe.getPermission().getName()) && (pe.getRevoked() != null && pe.getRevoked()))
            {
            	permissions.remove(pe.getPermission().getName());
            }
            if(!permissions.contains(pe.getPermission().getName()) && (pe.getRevoked()==null || !pe.getRevoked()))
            {
            	permissions.add(pe.getPermission().getName());
			
            }
         
		}
		
		return permissions
				.stream()
				.distinct()
				.collect(Collectors.toList());
	}

	// Pass the results of getPermissions method above to the method below
	private List<GrantedAuthority> getGrantedAuthorities(List<String> permissions) {
		List<GrantedAuthority> authorities = new ArrayList<>();

		if (!permissions.isEmpty()) {
			for (String permission : permissions) {
				authorities.add(new SimpleGrantedAuthority(permission));
			}
		}
		return authorities;
	}
}
