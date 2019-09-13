package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.AppRepository;
import com.stn.ester.repositories.jpa.base.traits.RepositoryListTrait;
import com.stn.ester.repositories.jpa.projections.IdNameList;
import com.stn.ester.entities.State;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends AppRepository<State>, RepositoryListTrait<IdNameList> {
    List<State> findAllByCountryId(long country_id);
}
