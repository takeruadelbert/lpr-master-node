package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.Department;
import com.stn.ester.rest.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/departments")
public class DepartmentController extends AppController<DepartmentService, Department> {

    private DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        super(departmentService);
        this.departmentService = departmentService;
    }
}