package com.nfinity.example179.domain.Flowable.Privileges;

import java.util.List;

public interface IActIdPrivManager {
void create(ActIdPrivEntity actIdPrivilege);

ActIdPrivEntity findByName(String name);

void delete(ActIdPrivEntity actIdPrivilege);

void update(ActIdPrivEntity actIdPrivilege);

void deleteAll();
}
