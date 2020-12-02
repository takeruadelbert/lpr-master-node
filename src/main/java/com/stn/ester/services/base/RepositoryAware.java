package com.stn.ester.services.base;

import com.stn.ester.repositories.jpa.base.BaseRepository;

public interface RepositoryAware<T extends BaseRepository> {

    T getRepository();
}
