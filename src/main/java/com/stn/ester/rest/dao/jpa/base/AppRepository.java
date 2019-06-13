package com.stn.ester.rest.dao.jpa.base;

import com.stn.ester.rest.dao.jpa.projections.OptionList;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface AppRepository<U, T, Y extends OptionList> extends PagingAndSortingRepository<U, T>, ProjectionRepoBehaviour<Y> {

}
