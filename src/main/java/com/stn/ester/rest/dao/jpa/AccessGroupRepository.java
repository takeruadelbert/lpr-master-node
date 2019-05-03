package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.domain.AccessGroup;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessGroupRepository extends PagingAndSortingRepository<AccessGroup, Long> {
}
