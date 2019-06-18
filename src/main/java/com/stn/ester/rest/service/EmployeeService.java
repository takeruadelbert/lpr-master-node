package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.EmployeeRepository;
import com.stn.ester.rest.dao.jpa.PositionRepository;
import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.domain.Employee;
import com.stn.ester.rest.domain.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class EmployeeService extends AppService {

    private PositionRepository positionRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, PositionRepository positionRepository) {
        super(Employee.unique_name);
        super.repositories.put(Employee.unique_name, employeeRepository);
        this.positionRepository = positionRepository;
    }

    @Override
    @Transactional
    public Object create(AppDomain domain) {
        // fetch data user group by selected position and then append it into User object.
        long position_id = ((Employee) domain).getPositionId();
        Position position = this.positionRepository.findById(position_id).get();
        long user_group_id = position.getUserGroupId();
        ((Employee) domain).getUser().setUserGroupId(user_group_id);

        // append data Employee Work Status
        ((Employee) domain).setEmployeeWorkStatusId(1); // -> default is Active
        return super.create(domain);
    }
}
