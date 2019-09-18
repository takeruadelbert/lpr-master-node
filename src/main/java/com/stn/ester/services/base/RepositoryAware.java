package com.stn.ester.services.base;

import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.repositories.jpa.base.BaseRepository;

public interface RepositoryAware<U extends BaseEntity, T extends BaseRepository> {

    T getRepository();
}
