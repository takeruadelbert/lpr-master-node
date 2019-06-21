package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdNameList;
import com.stn.ester.rest.domain.City;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends AppRepository<City, IdNameList>{
    List<City> findAllByStateId(long state_id);
}
