package com.nfinity.example179.RestControllers; 
 
import com.nfinity.example179.application.Authorization.User.UserAppService; 
import com.nfinity.example179.domain.Authorization.User.IUserManager; 
import com.nfinity.example179.domain.model.UserEntity; 
import com.nfinity.example179.security.ConvertToPrivilegeAuthorities; 
import com.nfinity.example179.CommonModule.domain.EmptyJsonResponse;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.core.env.Environment; 
import javax.naming.NamingException; 
 
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity; 
import org.springframework.ldap.core.AttributesMapper; 
import org.springframework.ldap.core.LdapTemplate; 
import org.springframework.ldap.core.support.LdapContextSource; 
import org.springframework.security.core.context.SecurityContextHolder; 
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RequestMethod; 
import org.springframework.web.bind.annotation.RestController; 
import org.springframework.web.context.request.RequestContextHolder; 
import org.springframework.web.context.request.ServletRequestAttributes; 
 
import javax.naming.directory.Attribute; 
import javax.naming.directory.Attributes; 
import javax.servlet.http.HttpServletResponse; 
import java.io.IOException; 
import java.io.PrintWriter; 
import java.util.List; 
 
@RestController 
@RequestMapping("/auth") 
public class LoginController { 
 
    @Autowired 
    private ConvertToPrivilegeAuthorities connection; 
 
    @Autowired 
    private Environment env; 
 
    @Autowired 
    private UserAppService userAppService; 
 
    @Autowired 
    private IUserManager _userMgr; 
 
 
    @RequestMapping(value = "/logout", method = RequestMethod.POST) 
    public ResponseEntity logout() throws Exception{ 
 
        String userName = SecurityContextHolder.getContext().getAuthentication().getName(); 
        UserEntity userEntity = _userMgr.FindByUserName(userName); 
 
        userAppService.deleteAllUserTokens(userEntity.getUserName()); 
 
        return new ResponseEntity(new EmptyJsonResponse(), HttpStatus.OK); 
    } 
 
 
//    @RequestMapping("/oidc") 
//    public void securedPageOIDC(Model model, OAuth2AuthenticationToken authentication) { 
// 
//        connection.getJwtToken((List<String>) authentication.getPrincipal().getAttributes().get("groups"), (String) authentication.getPrincipal().getAttributes().get("preferred_username")); 
// 
//    } 
 
    @RequestMapping("/getLdapUsers") 
    public void getUsers() throws Exception { 
 
        LdapContextSource ldapContextSource = new LdapContextSource(); 
        ldapContextSource.setUrl(env.getProperty("fastCode.ldap.contextsourceurl")); 
        ldapContextSource.setUserDn(env 
                .getProperty("fastCode.ldap.manager.dn")); 
        ldapContextSource.setPassword(env 
                .getProperty("fastCode.ldap.manager.password")); 
        ldapContextSource.afterPropertiesSet(); 
 
        LdapTemplate template = new LdapTemplate(ldapContextSource); 
        template.afterPropertiesSet(); 
 
 
        List<Person> lp = template.search("", "(objectclass=person)", new PersonAttributesMapper()); 
 
        HttpServletResponse res = 
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()) 
                        .getResponse(); 
        res.setContentType("application/json"); 
 
        PrintWriter out = null; 
        try { 
            out = res.getWriter(); 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
        out.println(); 
        out.println(lp.toArray().toString()); 
        out.println("}"); 
        out.close(); 
 
    } 
 
    private class PersonAttributesMapper implements AttributesMapper{ 
 
        @Override 
        public Object mapFromAttributes(Attributes attributes) throws NamingException { 
            LoginController.Person person = new LoginController.Person(); 
 
            Attribute name = attributes.get("name"); 
            if (name != null){ 
                person.setName((String) name.get()); 
            } 
 
            Attribute displayname = attributes.get("displayname"); 
            if (displayname != null){ 
                person.setDisplayName((String) displayname.get()); 
            } 
 
            Attribute lastname = attributes.get("sn"); 
            if (lastname != null){ 
                person.setLastName((String) lastname.get()); 
            } 
 
            Attribute firstname = attributes.get("cn"); 
            if (firstname != null){ 
                person.setFirstName((String) firstname.get()); 
            } 
 
            Attribute mail = attributes.get("mail"); 
            if (mail != null){ 
                person.setMail((String) mail.get()); 
            } 
 
            Attribute userid = attributes.get("uid"); 
            if (userid != null){ 
                person.setUserID((String) userid.get()); 
            } 
 
            System.out.println(person.toString()); 
 
            return person; 
        } 
    } 
 
    private class Person { 
 
        private String name; 
        private String displayName; 
        private String lastName; 
        private String firstName; 
        private String mail; 
        private String userID; 
 
        public String getName() { 
            return name; 
        } 
 
        public void setName(String name) { 
            this.name = name; 
        } 
 
        public String getDisplayName() { 
            return displayName; 
        } 
 
        public void setDisplayName(String displayName) { 
            this.displayName = displayName; 
        } 
 
        public String getLastName() { 
            return lastName; 
        } 
 
        public void setLastName(String lastName) { 
            this.lastName = lastName; 
        } 
 
        public String getFirstName() { 
            return firstName; 
        } 
 
        public void setFirstName(String firstName) { 
            this.firstName = firstName; 
        } 
 
        public String getMail() { 
            return mail; 
        } 
 
        public void setMail(String mail) { 
            this.mail = mail; 
        } 
 
        public String getUserID() { 
            return userID; 
        } 
 
        public void setUserID(String userID) { 
            this.userID = userID; 
        } 
 
        @Override 
        public String toString() { 
            return "Person{" + 
                    "name='" + name + '\'' + 
                    ", displayName='" + displayName + '\'' + 
                    ", lastName='" + lastName + '\'' + 
                    ", firstName='" + firstName + '\'' + 
                    ", mail='" + mail + '\'' + 
                    ", userID='" + userID + '\'' + 
                    '}'; 
        } 
    } 
 
 
} 