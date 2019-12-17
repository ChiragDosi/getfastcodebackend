package com.nfinity.example179;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment; 
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.nfinity.example179.security.JWTAuthenticationFilter;
import com.nfinity.example179.security.JWTAuthorizationFilter;

import static com.nfinity.example179.security.SecurityConstants.CONFIRM;
import static com.nfinity.example179.security.SecurityConstants.REGISTER;
import javax.naming.AuthenticationNotSupportedException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
	private Environment env;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {

      /*   if (env.getProperty("fastCode.auth.method").equalsIgnoreCase("oidc") ) {
            // The following configuration is for SSO
           http
                    .cors()
                    .and()
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/content-api/**","/dmn-api/**","/form-api/**","/app-api/**","/cmmn-api/**","/process-api/**","/v2/api-docs", "/actuator/**","/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**", "/swagger-resources/configuration/ui", "/swagger-resources/configuration/security", "/browser/index.html#", "/browser/**").permitAll()
                    .antMatchers(HttpMethod.POST, REGISTER).permitAll()
                    .antMatchers(HttpMethod.POST, CONFIRM).permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .oauth2Login();

        }

        // The following authorization configuration is for database/LDAP

        else {*/

            http
                    .cors()
                    .and()
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                    .authorizeRequests()
                    .antMatchers("/content-api/**","/dmn-api/**","/form-api/**","/app-api/**","/cmmn-api/**","/process-api/**","/v2/api-docs", "/actuator/**", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**", "/swagger-resources/configuration/ui", "/swagger-resources/configuration/security", "/browser/index.html#", "/browser/**").permitAll()
                    .antMatchers(HttpMethod.POST, REGISTER).permitAll()
                    .antMatchers(HttpMethod.POST, CONFIRM).permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .addFilter(new JWTAuthenticationFilter(authenticationManager()))
                    .addFilter(new JWTAuthorizationFilter(authenticationManager()));
      //  }
    }


    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        if((env.getProperty("fastCode.auth.method").equalsIgnoreCase("database") ||
                env.getProperty("fastCode.auth.method").equalsIgnoreCase("oidc"))) {
            auth
                    .userDetailsService(userDetailsService)
                    .passwordEncoder(new BCryptPasswordEncoder());
        }
        else if (env.getProperty("fastCode.auth.method").equalsIgnoreCase("ldap") ) {
            auth
                .ldapAuthentication()
                .contextSource()
                .url(env.getProperty("fastCode.ldap.contextsourceurl"))
                .managerDn(env.getProperty("fastCode.ldap.manager.dn"))
                .managerPassword(env.getProperty("fastCode.ldap.manager.password"))
                .and()
                .userSearchBase(env.getProperty("fastCode.ldap.usersearchbase"))
                .userSearchFilter(env.getProperty("fastCode.ldap.usersearchfilter"))
                .groupSearchBase(env.getProperty("fastCode.ldap.groupsearchbase"))
                .groupSearchFilter(env.getProperty("fastCode.ldap.groupsearchfilter"))
                .rolePrefix(env.getProperty("fastCode.ldap.roleprefix"));
        }

     /*   else if (env.getProperty("fastCode.auth.method").equalsIgnoreCase("oidc") || env.getProperty("fastCode.auth.method").equalsIgnoreCase("saml")) {

        }*/

        else {
            throw new AuthenticationNotSupportedException();
        }
    }

}
