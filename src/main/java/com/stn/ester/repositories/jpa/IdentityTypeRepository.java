package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.AppRepository;
import com.stn.ester.repositories.jpa.base.traits.RepositoryListTrait;
import com.stn.ester.repositories.jpa.projections.IdLabelList;
import com.stn.ester.entities.IdentityType;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdentityTypeRepository extends AppRepository<IdentityType>, RepositoryListTrait<IdLabelList> {
    Optional<IdentityType> findByLabel(String label);
}