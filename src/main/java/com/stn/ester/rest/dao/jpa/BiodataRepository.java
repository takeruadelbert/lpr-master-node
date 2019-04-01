package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiodataRepository extends PagingAndSortingRepository<User,Long> {
}
