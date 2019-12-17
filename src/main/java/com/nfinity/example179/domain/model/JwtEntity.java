package com.nfinity.example179.domain.model; 
 
import org.hibernate.validator.constraints.Length; 
import org.springframework.data.redis.core.RedisHash; 
import org.springframework.data.redis.core.index.Indexed; 
 
import javax.persistence.*; 
import javax.validation.constraints.Email; 
import javax.validation.constraints.NotNull; 
import java.io.Serializable; 
 
@RedisHash("JwtEntity") 
public class JwtEntity implements Serializable { 
 
    private Long id; 
    private String userName; 
    private @Indexed String token; 
    private Boolean isActive; 
 
    @Id 
    @Column(name = "Id", nullable = false) 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    public Long getId() { 
        return id; 
    } 
    public void setId(Long id) { 
        this.id = id; 
    } 
 
    @Basic 
    @Column(name = "UserName", nullable = false, length = 32) 
    @NotNull 
    @Length(max = 32, message = "The field must be less than 32 characters") 
    public String getUserName() { 
        return userName; 
    } 
    public void setUserName(String userName) { 
        this.userName = userName; 
    } 
 
    @Basic 
    @Column(name = "Token", nullable = false, length=2000) 
    @NotNull 
    @Length(max = 2000, message = "The field must be less than 2147483647 characters") 
    public String getToken() { 
        return token; 
    } 
    public void setToken(String token) { 
        this.token = token; 
    } 
 
    @Basic 
    @Column(name = "IsActive", nullable = false) 
    public Boolean getIsActive() { 
        return isActive; 
    } 
 
    public void setIsActive(Boolean active) { 
        isActive = active; 
    } 
 
    @Override 
    public boolean equals(Object o) { 
        if (this == o) return true; 
        if (!(o instanceof JwtEntity)) return false; 
        JwtEntity jwt = (JwtEntity) o; 
        return id != null && id.equals(jwt.id); 
    } 
 
    @Override 
    public int hashCode() { 
        return 31; 
    } 
 
} 