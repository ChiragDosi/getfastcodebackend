package com.nfinity.example179.domain.Flowable.Tokens;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "act_id_token", schema = "test8")
public class ActIdTokenEntity implements Serializable {

private String id;
private Long rev;
private String tokenValue;
private Date tokenDate;
private String ipAddress;
private String userAgent;
private String userId;
private String tokenData;

@Id
@Column(name = "id_", nullable = false, length = 64)
public String getId() {
return id;
}

public void setId(String id) {
this.id = id;
}

@Basic
@Column(name = "rev_", nullable = true)
public Long getRev() {
return rev;
}

public void setRev(Long rev) {
this.rev = rev;
}

@Basic
@Column(name = "token_value_", nullable = true, length = 255)
public String getTokenValue() {
return tokenValue;
}

public void setTokenValue(String tokenValue) {
this.tokenValue = tokenValue;
}
@Basic
@Column(name = "token_date_", nullable = true)
public Date getTokenDate() {
return tokenDate;
}

public void setTokenDate(Date tokenDate) {
this.tokenDate = tokenDate;
}

@Basic
@Column(name = "ip_address_", nullable = true, length = 255)
public String getIpAddress() {
return ipAddress;
}

public void setIpAddress(String ipAddress) {
this.ipAddress = ipAddress;
}

@Basic
@Column(name = "user_agent_", nullable = true, length = 255)
public String getUserAgent() {
return userAgent;
}

public void setUserAgent(String userAgent) {
this.userAgent = userAgent;
}

@Basic
@Column(name = "user_id_", nullable = true, length = 255)
public String getUserId() {
return userId;
}

public void setUserId(String userId) {
this.userId = userId;
}

@Basic
@Column(name = "token_data_", nullable = true, length = 2000)
public String getTokenData() {
return tokenData;
}

public void setTokenData(String tokenData) {
this.tokenData = tokenData;
}

/*public Object getPersistentState() {
Map<String, Object> persistentState = new HashMap<>();
persistentState.put("tokenValue", tokenValue);
persistentState.put("tokenDate", tokenDate);
persistentState.put("ipAddress", ipAddress);
persistentState.put("userAgent", userAgent);
persistentState.put("userId", userId);
persistentState.put("tokenData", tokenData);

return persistentState;
}*/

// common methods //////////////////////////////////////////////////////////
@Override
public boolean equals(Object o) {
if (this == o) return true;
if (!(o instanceof ActIdTokenEntity)) return false;
ActIdTokenEntity token = (ActIdTokenEntity) o;
return id != null && id.equals(token.id);
}
@Override
public String toString() {
return "TokenEntity[tokenValue=" + tokenValue + ", userId=" + userId + "]";
}

}
