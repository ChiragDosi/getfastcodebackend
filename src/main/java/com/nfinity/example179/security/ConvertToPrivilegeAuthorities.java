package com.nfinity.example179.security;

import com.nfinity.example179.domain.model.RolepermissionEntity;
import com.nfinity.example179.domain.Authorization.Role.IRoleManager;
import com.nfinity.example179.domain.model.RoleEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class ConvertToPrivilegeAuthorities {

    IRoleManager _roleManager = null;

    public void getJwtToken(List<String> groups, String userName) {

        Claims claims = Jwts.claims();
        ConvertToPrivilegeAuthorities con = new ConvertToPrivilegeAuthorities();

        String[] groupsArray = new String[groups.size()];

        List<GrantedAuthority> authorities = con.convert(AuthorityUtils.createAuthorityList(groups.toArray(groupsArray)));

        claims.put("scopes", authorities.stream().map(s -> s.toString()).collect(Collectors.toList()));
        claims.setSubject(userName);

        claims.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME));
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes())
                .compact();

        HttpServletResponse res =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getResponse();
        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        res.setContentType("application/json");

        PrintWriter out = null;
        try {
            out = res.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("{");
        out.println("\"token\":" + "\"" + SecurityConstants.TOKEN_PREFIX + token + "\"");
        out.println("}");
        out.close();

    }

    public List<GrantedAuthority> convert(Collection<? extends GrantedAuthority> authorities) {

        HttpServletRequest req =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();

        // We cannot autowire RolesManager, but need to use the code below to set it
        if (_roleManager == null) {
            ServletContext servletContext = req.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            _roleManager = webApplicationContext.getBean(IRoleManager.class);
        }


        List<GrantedAuthority> currentlistAuthorities = new ArrayList<GrantedAuthority>();
        currentlistAuthorities.addAll(authorities);

        List<GrantedAuthority> newlistAuthorities = new ArrayList<GrantedAuthority>();

        // Iterate through the list and check for ROLE_ authorities. We need to convert these into privileges and then remove duplicate privileges

        for (GrantedAuthority ga : currentlistAuthorities) {
            if (ga.getAuthority().startsWith("ROLE_")) {

                RoleEntity re = _roleManager.FindByRoleName(ga.getAuthority());
                Set<RolepermissionEntity> rpe=re.getRolepermissionSet();
//              Set<PermissionEntity> spe = re.getPermissions();
//              if (spe.size() != 0) {
//              	for (PermissionEntity pe : spe) {
//                  	SimpleGrantedAuthority authority = new SimpleGrantedAuthority(pe.getName());
//                      newlistAuthorities.add(authority);
//                  }
//              }
                if (rpe.size() != 0) {
                Iterator iterator = rpe.iterator();
   			 
    			while (iterator.hasNext()) { 
    				RolepermissionEntity pe = (RolepermissionEntity) iterator.next();
    				SimpleGrantedAuthority authority = new SimpleGrantedAuthority(pe.getPermission().getName());
                    newlistAuthorities.add(authority);	
    				}
    			}
            } else {

                newlistAuthorities.add(ga);
            }
        }
        return newlistAuthorities.stream()
                .distinct()
                .collect(Collectors.toList());

    }

}
