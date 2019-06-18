package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.EmployeeWorkStatus;
import com.stn.ester.rest.service.EmployeeWorkStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/employee_work_statuses")
public class EmployeeWorkStatusController extends AppController<EmployeeWorkStatusService, EmployeeWorkStatus> {
    @Autowired
    public EmployeeWorkStatusController(EmployeeWorkStatusService employeeWorkStatusService) {
        super(employeeWorkStatusService);
    }
}
