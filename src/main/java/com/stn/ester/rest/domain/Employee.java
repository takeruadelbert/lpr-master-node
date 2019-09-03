package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.stn.ester.rest.domain.constant.EntityConstant;
import com.stn.ester.rest.domain.enumerate.EmployeeWorkStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Entity
public class Employee extends AppDomain {
    public static String unique_name = "employee";
    private static final String COLUMN_MAPPED_BY_EMPLOYEE = "employee";
    private static final String COLUMN_DEPARTMENT = "department_id";
    private static final String COLUMN_POSITION = "positionId";
    private static final String JSON_PROPERTY_DEPARTMENT = "departmentId";
    private static final String JSON_PROPERTY_POSITION = "positionId";

    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
    @Column(nullable = false, unique = true)
    private String NIP;

    @Column(columnDefinition = EntityConstant.COLUMN_DEFINITION_DATE)
    @DateTimeFormat(pattern = EntityConstant.FORMAT_DEFAULT_DATE)
    private Date tmt;

    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY, mappedBy = COLUMN_MAPPED_BY_EMPLOYEE, cascade = CascadeType.ALL)
    @JsonManagedReference
    private User user;

    @Enumerated(EnumType.STRING)
    private EmployeeWorkStatus employeeWorkStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_DEPARTMENT, insertable = false, updatable = false)
    private Department department;

    @JsonProperty(JSON_PROPERTY_DEPARTMENT)
    @Column(name = COLUMN_DEPARTMENT)
    private Long departmentId;

    @JsonSetter(JSON_PROPERTY_DEPARTMENT)
    public void setDepartmentId(long departmentId) {
        if (departmentId != 0)
            this.departmentId = departmentId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_POSITION, insertable = false, updatable = false)
    private Position position;

    @JsonProperty(JSON_PROPERTY_POSITION)
    @Column(name = COLUMN_POSITION)
    private Long positionId;

    @JsonSetter(JSON_PROPERTY_POSITION)
    public void setPositionId(long positionId) {
        if (positionId != 0) {
            this.positionId = positionId;
        }
    }

    public Long getPositionId() {
        return this.positionId;
    }

    public User getUser() {
        return this.user;
    }

    public void setEmployeeWorkStatus(EmployeeWorkStatus employeeWorkStatus) {
        this.employeeWorkStatus = employeeWorkStatus;
    }

    public String underscoreName() {
        return unique_name;
    }
}
