package com.stn.ester.rest.controller.crud;

import com.stn.ester.rest.controller.base.CrudController;
import com.stn.ester.rest.domain.Employee;
import com.stn.ester.rest.domain.enumerate.EmployeeWorkStatus;
import com.stn.ester.rest.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(value = "/employees")
public class EmployeeController extends CrudController<EmployeeService, Employee> {
    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        super(employeeService);
    }

    @Override
    public Object create(@Valid @RequestBody Employee employee) {
        return service.create(employee);
    }

    @RequestMapping(value = "/work_status",method = RequestMethod.OPTIONS)
    public Map<EmployeeWorkStatus, String> getEmployeeWorkStatusList() {
        return service.getEmployeeWorkStatusList();
    }
}
