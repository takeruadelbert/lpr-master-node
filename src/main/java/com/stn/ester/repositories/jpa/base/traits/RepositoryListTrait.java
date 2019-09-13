package com.stn.ester.repositories.jpa.base.traits;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface RepositoryListTrait<T> {
    List<T> findAllProjectedBy();
}
