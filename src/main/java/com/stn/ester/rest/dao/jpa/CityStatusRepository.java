package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.domain.CityStatus;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityStatusRepository extends PagingAndSortingRepository<CityStatus, Long> {
}
