package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.Department;
import com.stn.ester.rest.service.DepartmentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/departments")
public class DepartmentController extends AppController<DepartmentService, Department> {
    public DepartmentController(DepartmentService departmentService) {
        super(departmentService);
    }
}
