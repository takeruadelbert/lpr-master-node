package com.stn.ester.repositories.jpa.base;

import com.stn.ester.entities.base.BaseEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface BaseRepository<U extends BaseEntity> extends PagingAndSortingRepository<U, Long>, JpaSpecificationExecutor<U> {

}
