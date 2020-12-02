package com.stn.ester.repositories.jpa;

import com.stn.ester.entities.State;
import com.stn.ester.repositories.jpa.base.BaseRepository;
import com.stn.ester.repositories.jpa.base.traits.RepositoryListTrait;
import com.stn.ester.repositories.jpa.projections.IdNameOption;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StateRepository extends BaseRepository<State>, RepositoryListTrait<IdNameOption> {
    List<State> findAllByCountryId(long country_id);
}
