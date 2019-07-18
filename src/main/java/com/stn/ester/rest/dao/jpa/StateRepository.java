package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdList;
import com.stn.ester.rest.dao.jpa.projections.IdNameList;
import com.stn.ester.rest.domain.State;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends AppRepository<State, IdNameList> {
    List<State> findAllByCountryId(long country_id);
}
