package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class State extends AppDomain {
    public static final String unique_name = "state";

    @NotBlank(message = "Name is mandatory.")
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", insertable = false, updatable = false)
    private Country country;

    @JsonProperty("countryId")
    @Column(name = "country_id")
    @NotNull(message = "Country is mandatory.")
    private Long countryId;

    @JsonSetter("countryId")
    public void setCountryId(long countryId) {
        if (countryId != 0)
            this.countryId = countryId;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String underscoreName() {
        return unique_name;
    }
}
