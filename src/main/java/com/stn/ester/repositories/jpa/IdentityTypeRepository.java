package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.BaseRepository;
import com.stn.ester.repositories.jpa.base.traits.RepositoryListTrait;
import com.stn.ester.repositories.jpa.projections.IdLabelOption;
import com.stn.ester.entities.IdentityType;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdentityTypeRepository extends BaseRepository<IdentityType>, RepositoryListTrait<IdLabelOption> {
    Optional<IdentityType> findByLabel(String label);
}