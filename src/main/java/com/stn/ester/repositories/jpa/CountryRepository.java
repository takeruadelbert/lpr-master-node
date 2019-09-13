package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.AppRepository;
import com.stn.ester.repositories.jpa.base.traits.RepositoryListTrait;
import com.stn.ester.repositories.jpa.projections.IdNameList;
import com.stn.ester.entities.Country;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends AppRepository<Country>, RepositoryListTrait<IdNameList> {
}
