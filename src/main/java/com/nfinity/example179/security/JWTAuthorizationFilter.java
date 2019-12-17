package com.nfinity.example179.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.nfinity.example179.CommonModule.error.ApiError;
import com.nfinity.example179.CommonModule.error.ExceptionMessageConstants;
import com.nfinity.example179.CommonModule.logging.LoggingHelper;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.stream.Collectors;
import java.net.URL;
import org.springframework.security.core.authority.AuthorityUtils;
import com.nfinity.example179.domain.IRepository.IJwtRepository;
import com.nfinity.example179.domain.model.JwtEntity;
import com.nfinity.example179.domain.model.UserpermissionEntity;
import com.nfinity.example179.domain.model.UserroleEntity;
import com.nfinity.example179.domain.model.RolepermissionEntity;
import com.nfinity.example179.domain.model.RoleEntity;
import com.nfinity.example179.domain.Authorization.User.IUserManager;
import com.nfinity.example179.domain.model.UserEntity;
import com.nimbusds.jose.*;
import com.nimbusds.jwt.*;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import java.util.*;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    //@Autowired
    private Environment environment;
    
    private IJwtRepository jwtRepo;

    private IUserManager _userMgr;

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(SecurityConstants.HEADER_STRING);

        if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }


        UsernamePasswordAuthenticationToken authentication = null;
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED);
        LoggingHelper logHelper = new LoggingHelper();
        try {
            authentication = getAuthentication(req);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(req, res);
            return;

        } catch (ExpiredJwtException exception) {
            apiError.setMessage(ExceptionMessageConstants.TOKEN_EXPIRED);
            logHelper.getLogger().error("An Exception Occurred:", exception);
            res.setStatus(401);
        } catch (UnsupportedJwtException exception) {
            apiError.setMessage(ExceptionMessageConstants.TOKEN_UNSUPPORTED);
            logHelper.getLogger().error("An Exception Occurred:", exception);
            res.setStatus(401);
        } catch (MalformedJwtException exception) {
            apiError.setMessage(ExceptionMessageConstants.TOKEN_MALFORMED);
            logHelper.getLogger().error("An Exception Occurred:", exception);
            res.setStatus(401);
        } catch (SignatureException exception) {
            apiError.setMessage(ExceptionMessageConstants.TOKEN_INCORRECT_SIGNATURE);
            logHelper.getLogger().error("An Exception Occurred:", exception);
            res.setStatus(401);
        } catch (IllegalArgumentException exception) {
            apiError.setMessage(ExceptionMessageConstants.TOKEN_ILLEGAL_ARGUMENT);
            logHelper.getLogger().error("An Exception Occurred:", exception);
            res.setStatus(401);
        } catch (JwtException exception) {
             apiError.setMessage(ExceptionMessageConstants.TOKEN_UNAUTHORIZED);
             logHelper.getLogger().error("An Exception Occurred:", exception);
             res.setStatus(401);
	    }


        OutputStream out = res.getOutputStream();
        com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, apiError);
        out.flush();
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }


    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws JwtException {

        String token = request.getHeader(SecurityConstants.HEADER_STRING);
        
        if(jwtRepo==null){
             ServletContext servletContext = request.getServletContext();
             WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
             jwtRepo = webApplicationContext.getBean(IJwtRepository.class);
         }
 
         // Check that the token is inactive in the JwtEntity table
 
         JwtEntity jwt = jwtRepo.findByToken(token);
         ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED);
 
         if(jwt == null) {
             throw new JwtException("Token Does Not Exist");
         }
 
         if(!jwt.getIsActive()) {
             throw new JwtException("Token Inactive");
	     }
        
        Claims claims;
        if(environment==null){
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            environment = webApplicationContext.getBean(Environment.class);
        }
        if (StringUtils.isNotEmpty(token) && token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            String userName = null;
            List<GrantedAuthority> authorities = null;
            if(!environment.getProperty("fastCode.auth.method").equals("oidc")) {

                 claims = Jwts.parser()
                        .setSigningKey(SecurityConstants.SECRET.getBytes())
                        .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                        .getBody();
                userName = claims.getSubject();
                List<String> scopes = claims.get("scopes", List.class);
                authorities = scopes.stream()
                        .map(authority -> new SimpleGrantedAuthority(authority))
                        .collect(Collectors.toList());
            }
            else{

	            SignedJWT accessToken = null;
	            JWTClaimsSet claimSet = null;
	
	            try {
	              
	                accessToken = SignedJWT.parse(token.replace(SecurityConstants.TOKEN_PREFIX, ""));
	            
	                String kid = accessToken.getHeader().getKeyID();
	                
	                JWKSet jwks = null;
					jwks = JWKSet.load(new URL(environment.getProperty("spring.security.oauth2.client.provider.oidc.issuer-uri") + "/v1/keys"));
	
	                RSAKey jwk = (RSAKey) jwks.getKeyByKeyId(kid);
	
	                JWSVerifier verifier = new RSASSAVerifier(jwk);
	
	                if (accessToken.verify(verifier)) {
	                    System.out.println("valid signature");
	                    claimSet = accessToken.getJWTClaimsSet();
	                    userName = claimSet.getSubject();
	                } else {
	                    System.out.println("invalid signature");
	                }
	                } catch (Exception e) {
	                    e.printStackTrace();
	            }
                if(_userMgr==null){
                    ServletContext servletContext = request.getServletContext();
                    WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
                    _userMgr = webApplicationContext.getBean(IUserManager.class);
                }

                // Add all the roles and permissions in a list and then convert the list into all permissions, removing duplicates

                UserEntity user = _userMgr.FindByUserName(userName);
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

        		String[] groupsArray = new String[permissions.size()];
               // ConvertToPrivilegeAuthorities con = new ConvertToPrivilegeAuthorities();
               // authorities = con.convert(AuthorityUtils.createAuthorityList(groups.toArray(groupsArray)));
        		authorities = AuthorityUtils.createAuthorityList(permissions.toArray(groupsArray));
            }

            if ((userName != null) && StringUtils.isNotEmpty(userName)) {
            	return new UsernamePasswordAuthenticationToken(userName, null, authorities);
        	}
        }
        return null;

    }

}