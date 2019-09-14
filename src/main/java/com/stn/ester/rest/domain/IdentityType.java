package com.stn.ester.rest.domain;

import com.stn.ester.rest.domain.constant.EntityConstant;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class IdentityType extends AppDomain {

    public static final String unique_name = "identity_type";

    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
    public String label;

    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
    @Column(nullable = false)
    private String name;

    @Override
    public String underscoreName() {
        return null;
    }
}
