package com.stn.ester.rest.dao.jpa;

import com.stn.ester.rest.dao.jpa.base.AppRepository;
import com.stn.ester.rest.dao.jpa.projections.IdList;
import com.stn.ester.rest.domain.Employee;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends AppRepository<Employee, IdList> {
}
