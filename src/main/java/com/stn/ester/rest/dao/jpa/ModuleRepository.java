package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.domain.Module;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends PagingAndSortingRepository<Module,Long> {
}
