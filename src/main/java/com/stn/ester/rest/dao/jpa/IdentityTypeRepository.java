package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.domain.IdentityType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdentityTypeRepository extends PagingAndSortingRepository<IdentityType,Long> {
    Optional<IdentityType> findByLabel(String label);
}