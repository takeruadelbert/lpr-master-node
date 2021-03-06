package com.stn.ester.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.constant.EntityConstant;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class State extends BaseEntity {
    private static final String COLUMN_COUNTRY = "country_id";
    private static final String JSON_PROPERTY_COUNTRY = "countryId";

    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_COUNTRY, insertable = false, updatable = false)
    private Country country;

    @JsonProperty(JSON_PROPERTY_COUNTRY)
    @Column(name = COLUMN_COUNTRY)
    @NotNull(message = EntityConstant.MESSAGE_NOT_BLANK)
    private Long countryId;

    @JsonSetter(JSON_PROPERTY_COUNTRY)
    public void setCountryId(long countryId) {
        if (countryId != 0)
            this.countryId = countryId;
    }

    public String getName() {
        return this.name;
    }

}
