package com.stn.ester.rest.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class EmployeeWorkStatus extends AppDomain {
    public static String unique_name = "employee_work_status";

    @NotBlank(message = "Name is mandatory.")
    @Column(nullable = false)
    private String name;

    public String underscoreName() {
        return unique_name;
    }
}
