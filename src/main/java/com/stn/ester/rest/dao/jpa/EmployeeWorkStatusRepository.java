package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdNameList;
import com.stn.ester.rest.domain.EmployeeWorkStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeWorkStatusRepository extends AppRepository<EmployeeWorkStatus, IdNameList> {
}