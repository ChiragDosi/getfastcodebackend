package com.nfinity.example179.domain.Flowable.Tokens;

import com.nfinity.example179.domain.IRepository.IActIdTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ActIdTokenManager implements IActIdTokenManager {

@Autowired
private IActIdTokenRepository _tokensRepository;

public ActIdTokenEntity generateNewToken(String tokenId) {
ActIdTokenEntity tokenEntity = new ActIdTokenEntity();
tokenEntity.setId(tokenId);
tokenEntity.setRev(0L); // needed as tokens can be transient
return tokenEntity;
}

@Override
public void insertToken(ActIdTokenEntity token) {
_tokensRepository.save(token);
}

@Override
public void updateToken(ActIdTokenEntity token) {
_tokensRepository.save(token);
}

@Override
public boolean isNewToken(ActIdTokenEntity token) {
return token.getRev() == 0;
}
}
