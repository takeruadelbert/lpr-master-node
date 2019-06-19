package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@Entity
public class Employee extends AppDomain {
    public static String unique_name = "employee";

    @NotBlank(message = "NIP is mandatory")
    @Column(nullable = false, unique = true)
    private String NIP;

    @Column(columnDefinition = "DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tmt;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.ALL)
    @JsonManagedReference
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_work_status_id", insertable = false, updatable = false)
    private EmployeeWorkStatus employeeWorkStatus;

    @JsonProperty("employeeWorkStatusId")
    @Column(name = "employee_work_status_id")
    private Long employeeWorkStatusId;

    @JsonSetter("employeeWorkStatusId")
    public void setEmployeeWorkStatusId(long employeeWorkStatusId) {
        if (employeeWorkStatusId != 0)
            this.employeeWorkStatusId = employeeWorkStatusId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", insertable = false, updatable = false)
    private Department department;

    @JsonProperty("departmentId")
    @Column(name = "department_id")
    private Long departmentId;

    @JsonSetter("departmentId")
    public void setDepartmentId(long departmentId) {
        if (departmentId != 0)
            this.departmentId = departmentId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "position_id", insertable = false, updatable = false)
    private Position position;

    @JsonProperty("positionId")
    @Column(name = "position_id")
    private Long positionId;

    @JsonSetter("positionId")
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

    public String underscoreName() {
        return unique_name;
    }
}
