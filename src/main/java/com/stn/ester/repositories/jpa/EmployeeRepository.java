package com.stn.ester.repositories.jpa;

import com.stn.ester.repositories.jpa.base.AppRepository;
import com.stn.ester.entities.Employee;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends AppRepository<Employee> {
}
