package com.stn.ester.services.base;

import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.repositories.jpa.base.AppRepository;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseService<U extends BaseEntity> {
    protected Map<String, AppRepository> repositories = new HashMap<>();
}
