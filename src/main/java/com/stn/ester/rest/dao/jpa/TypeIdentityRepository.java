package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.domain.TypeIdentity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeIdentityRepository extends PagingAndSortingRepository<TypeIdentity,Long> {
    Optional<TypeIdentity> findByLabel(String label);
}
