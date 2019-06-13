package com.stn.ester.rest.dao.jpa.base;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface ProjectionRepoBehaviour<T> {
    List<T> findAllProjectedBy();
}
