package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.EmployeeWorkStatusRepository;
import com.stn.ester.rest.domain.EmployeeWorkStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeWorkStatusService extends AppService {
    @Autowired
    public EmployeeWorkStatusService(EmployeeWorkStatusRepository employeeWorkStatusRepository) {
        super(EmployeeWorkStatus.unique_name);
        super.repositories.put(EmployeeWorkStatus.unique_name, employeeWorkStatusRepository);
    }

}
