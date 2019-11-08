package com.stn.ester.services.crud;

import com.stn.ester.core.events.RegistrationEvent;
import com.stn.ester.entities.Employee;
import com.stn.ester.entities.Position;
import com.stn.ester.entities.enumerate.EmployeeWorkStatus;
import com.stn.ester.repositories.jpa.EmployeeRepository;
import com.stn.ester.repositories.jpa.PositionRepository;
import com.stn.ester.repositories.jpa.UserRepository;
import com.stn.ester.services.base.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService extends CrudService<Employee, EmployeeRepository> {

    private PositionRepository positionRepository;
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final String defaultPassword = "password123";

    @Autowired
    public EmployeeService(
            EmployeeRepository employeeRepository,
            PositionRepository positionRepository,
            UserRepository userRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        super(employeeRepository);
        this.positionRepository = positionRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public Employee create(Employee employee) {

        // fetch data user group by selected position and then append it into User object.
        Long position_id = employee.getPositionId();
        Position position = this.positionRepository.findById(position_id).get();
        employee.getUser().addRole(position.getRole());

        if (employee.getUser().getToken() == null) {
            employee.getUser().setProfilePictureId(AssetFileService.defaultProfilePictureID); // set default profile picture
        }

        // append data Employee Work NewsStatus
        employee.setEmployeeWorkStatus(EmployeeWorkStatus.ACTIVE); // -> default is Active

        // encode password
        employee.getUser().setPassword(this.passwordEncoder.encode(this.defaultPassword));
        Employee createdEmployee = super.create(employee);
        eventPublisher.publishEvent(new RegistrationEvent(this, createdEmployee.getUser()));
        return createdEmployee;
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
