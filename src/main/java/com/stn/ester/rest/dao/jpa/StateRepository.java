package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.domain.State;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StateRepository extends PagingAndSortingRepository<State, Long> {
}
