package com.stn.ester.repositories.jpa;

import com.stn.ester.entities.City;
import com.stn.ester.repositories.jpa.base.BaseRepository;
import com.stn.ester.repositories.jpa.base.traits.RepositoryListTrait;
import com.stn.ester.repositories.jpa.projections.IdNameOption;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends BaseRepository<City>, RepositoryListTrait<IdNameOption> {
    List<City> findAllByStateId(long state_id);
}
