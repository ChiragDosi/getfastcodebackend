package com.nfinity.example179.domain.Flowable.Privileges;

import com.nfinity.example179.domain.IRepository.IActIdPrivRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ActIdPrivManager implements IActIdPrivManager {
@Autowired
private IActIdPrivRepository _privRepository;

@Override
public void create(ActIdPrivEntity actIdPrivilege) {
_privRepository.save(actIdPrivilege);
}

@Override
public ActIdPrivEntity findByName(String name) {
return _privRepository.findByName(name);
}

@Override
public void delete(ActIdPrivEntity actIdPrivilege) {
_privRepository.delete(actIdPrivilege);
}

@Override
public void update(ActIdPrivEntity actIdPrivilege) {
_privRepository.save(actIdPrivilege);
}

@Override
public void deleteAll() {
_privRepository.deleteAll();
}
}
