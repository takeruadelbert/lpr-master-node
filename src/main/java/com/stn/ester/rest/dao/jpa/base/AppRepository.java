package com.stn.ester.rest.dao.jpa.base;

import com.stn.ester.rest.dao.jpa.projections.OptionList;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface AppRepository<U, Y extends OptionList> extends PagingAndSortingRepository<U, Long>, ProjectionRepoBehaviour<Y>, JpaSpecificationExecutor<U> {

}
