package com.stn.ester.rest.domain;

import com.stn.ester.rest.domain.constant.EntityConstant;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class Country extends AppDomain {
    public static final String unique_name = "country";

    private String iso;

    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
    @Column(nullable = false, unique = true)
    private String name;

    private String iso3;

    @Override
    public String underscoreName() {
        return unique_name;
    }
}
