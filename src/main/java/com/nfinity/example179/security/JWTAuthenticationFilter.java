package com.nfinity.example179.security;

import com.nfinity.example179.application.Authorization.User.Dto.LoginUserInput;
import com.nfinity.example179.application.Flowable.FlowableIdentityService;
import com.nfinity.example179.domain.IRepository.IJwtRepository;
import com.nfinity.example179.domain.model.JwtEntity;
import com.nfinity.example179.domain.model.RolepermissionEntity;
import com.nfinity.example179.domain.Authorization.Role.IRoleManager;
import com.nfinity.example179.domain.model.RoleEntity;
import com.nfinity.example179.domain.Authorization.Role.RoleManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.ldap.userdetails.LdapUserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    IRoleManager _roleManager;
    IJwtRepository jwtRepo;
    FlowableIdentityService idmIdentityService;
    private static final String COOKIE_NAME = "FLOWABLE_REMEMBER_ME";
    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            System.out.println("I am here ...");
            LoginUserInput creds = new ObjectMapper()
                    .readValue(req.getInputStream(), LoginUserInput.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUserName(),
                            creds.getPassword(),
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        String cookieValue = null;
        // We cannot autowire RolesManager, but need to use the code below to set it
        if(_roleManager==null){
            ServletContext servletContext = req.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            _roleManager = webApplicationContext.getBean(RoleManager.class);
        }

        Claims claims = Jwts.claims();
       // claims.put("scopes", (convertToPrivilegeAuthorities(auth.getAuthorities())).stream().map(s -> s.toString()).collect(Collectors.toList()));
        claims.put("scopes", (auth.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList())));
        //Flowable IDM Support
        if(idmIdentityService==null){
            ServletContext servletContext = req.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            idmIdentityService = webApplicationContext.getBean(FlowableIdentityService.class);
        }
        if (auth != null) {
            String userId = "";
            if (auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
                userId = ((User) auth.getPrincipal()).getUsername();
                claims.setSubject(userId);
            }
            else if (auth.getPrincipal() instanceof LdapUserDetailsImpl) {
                userId = ((LdapUserDetailsImpl) auth.getPrincipal()).getUsername();
                claims.setSubject(userId);
            }
            //Flowable IDM Support
            if(userId != "") {
                cookieValue = idmIdentityService.createTokenAndCookie(userId, req, res);
            }
        }

        claims.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME));
        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET.getBytes())
                .compact();
                
        // Add the user and token to the JwtEntity table 
 
        JwtEntity jt = new JwtEntity(); 
        jt.setToken("Bearer "+ token); 
 
        User appUser = (User) auth.getPrincipal(); 
        jt.setUserName(appUser.getUsername()); 
 
        jt.setIsActive(true); 
 
        if(jwtRepo==null){ 
            ServletContext servletContext = req.getServletContext(); 
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext); 
            jwtRepo = webApplicationContext.getBean(IJwtRepository.class); 
        } 
 
        jwtRepo.save(jt); 
        
        res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
        res.setContentType("application/json");

        PrintWriter out = res.getWriter();
        out.println("{");
        out.println("\"token\":" + "\"" + SecurityConstants.TOKEN_PREFIX + token + "\"");
        if(cookieValue != null) {
            out.println(",\"" + COOKIE_NAME + "\":" + "\"" + cookieValue + "\"");
        }
        out.println("}");
        out.close();


//        String responseToClient= "{"+ "token:"+ TOKEN_PREFIX + token + "}";
//        res.getWriter().write(responseToClient);
        //res.getWriter().flush();
    }

//    private List<GrantedAuthority> convertToPrivilegeAuthorities(Collection<? extends GrantedAuthority> authorities) {
//
//        List<GrantedAuthority> currentlistAuthorities = new ArrayList<GrantedAuthority>();
//        currentlistAuthorities.addAll(authorities);
//
//        List<GrantedAuthority> newlistAuthorities = new ArrayList<GrantedAuthority>();
//
//        // Iterate through the list and check for ROLE_ authorities. We need to convert these into privileges and then remove duplicate privileges
//
//        for (GrantedAuthority ga : currentlistAuthorities) {
//        	System.out.println(" Authorities " + ga.getAuthority());
//            if (ga.getAuthority().startsWith("ROLE_")) {
//
//                RoleEntity re = _roleManager.FindByRoleName(ga.getAuthority());
//                Set<RolepermissionEntity> spe= re.getRolepermissionSet();
//                if(spe.size() != 0) {
//                    for (RolepermissionEntity pe : spe) {
//                        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(pe.getPermission().getName());
//                        newlistAuthorities.add(authority);
//                    }
//                }
////                Set<PermissionEntity> spe = re.getPermissions();
////                if(spe.size() != 0) {
////                    for (PermissionEntity pe : spe) {
////                        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(pe.getName());
////                        newlistAuthorities.add(authority);
////                    }
////                }
//            }
//
//            else {
//
//                newlistAuthorities.add(ga);
//            }
//        }
//        return newlistAuthorities.stream()
//                .distinct()
//                .collect(Collectors.toList());
//
//    }


}
