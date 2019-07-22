package com.stn.ester.rest.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class IdentityType extends AppDomain {

    public static final String unique_name = "identity_type";

    @NotBlank(message = "Label is mandatory.")
    public String label;

    @NotBlank(message = "Name is mandatory.")
    @Column(nullable = false)
    private String name;

    @Override
    public String underscoreName() {
        return null;
    }
}
