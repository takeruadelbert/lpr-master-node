package com.stn.ester.services.base;

import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.repositories.jpa.base.AppRepository;

import java.util.HashMap;

public abstract class BaseService<U extends BaseEntity> {
    protected HashMap<String, AppRepository> repositories = new HashMap<>();
}
