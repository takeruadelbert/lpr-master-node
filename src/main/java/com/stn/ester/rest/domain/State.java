package com.stn.ester.rest.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class State extends AppDomain {
    public static final String unique_name = "state";

    @NotBlank(message = "Name is mandatory.")
    @Column(nullable = false, unique = true)
    private String name;

    @Override
    public String underscoreName() {
        return unique_name;
    }
}
