package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.domain.LoginSession;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LoginSessionRepository extends PagingAndSortingRepository<LoginSession,Long> {
}
