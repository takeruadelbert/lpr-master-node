package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdLabelList;
import com.stn.ester.rest.domain.IdentityType;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdentityTypeRepository extends AppRepository<IdentityType, IdLabelList> {
    Optional<IdentityType> findByLabel(String label);
}