package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.EmployeeRepository;
import com.stn.ester.rest.dao.jpa.PositionRepository;
import com.stn.ester.rest.dao.jpa.UserRepository;
import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.domain.Employee;
import com.stn.ester.rest.domain.Position;
import com.stn.ester.rest.domain.User;
import com.stn.ester.rest.domain.enumerate.EmployeeWorkStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService extends AppService {

    private PositionRepository positionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final String defaultPassword = "password123";

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, PositionRepository positionRepository, UserRepository userRepository) {
        super(Employee.unique_name);
        super.repositories.put(Employee.unique_name, employeeRepository);
        super.repositories.put(User.unique_name, userRepository);
        this.positionRepository = positionRepository;
    }

    @Override
    @Transactional
    public Object create(AppDomain domain) {
        Employee employee = ((Employee) domain);

        // fetch data user group by selected position and then append it into User object.
        long position_id = employee.getPositionId();
        Position position = this.positionRepository.findById(position_id).get();
        long user_group_id = position.getUserGroupId();
        employee.getUser().setUserGroupId(user_group_id);

        if (employee.getUser().getToken() == null) {
            employee.getUser().setProfilePictureId(AssetFileService.defaultProfilePictureID); // set default profile picture
        }

        // append data Employee Work NewsStatus
        employee.setEmployeeWorkStatus(EmployeeWorkStatus.ACTIVE); // -> default is Active

        // encode password
        employee.getUser().setPassword(this.passwordEncoder.encode(this.defaultPassword));
        return super.create(employee);
    }

    public Map<EmployeeWorkStatus, String> getEmployeeWorkStatusList() {
        Map<EmployeeWorkStatus, String> result = new HashMap<>();
        List<EmployeeWorkStatus> employeeWorkStatuses = EmployeeWorkStatus.toList();
        for (EmployeeWorkStatus employeeWorkStatus : employeeWorkStatuses) {
            result.put(employeeWorkStatus, employeeWorkStatus.getLabel());
        }
        return result;
    }
}
