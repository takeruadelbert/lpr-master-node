package com.stn.ester.rest.domain;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class CityStatus extends AppDomain {
    public static final String unique_name = "city_status";

    @NotBlank(message = "Name is mandatory.")
    @Column(nullable = false, unique = true)
    private String name;

    private String alias;

    @Override
    public String underscoreName() {
        return unique_name;
    }
}
