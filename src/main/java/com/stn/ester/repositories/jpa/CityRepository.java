package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.AppRepository;
import com.stn.ester.repositories.jpa.base.traits.RepositoryListTrait;
import com.stn.ester.repositories.jpa.projections.IdNameList;
import com.stn.ester.entities.City;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends AppRepository<City>, RepositoryListTrait<IdNameList> {
    List<City> findAllByStateId(long state_id);
}
