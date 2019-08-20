package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
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

    @NotBlank(message = "NIP is mandatory")
    @Column(nullable = false, unique = true)
    private String NIP;

    @Column(columnDefinition = "DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tmt;

    @EqualsAndHashCode.Exclude
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "employee", cascade = CascadeType.ALL)
    @JsonManagedReference
    private User user;

    @Enumerated(EnumType.STRING)
    private EmployeeWorkStatus employeeWorkStatus;

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

    public void setEmployeeWorkStatus(EmployeeWorkStatus employeeWorkStatus) {
        this.employeeWorkStatus = employeeWorkStatus;
    }

    public String underscoreName() {
        return unique_name;
    }
}
