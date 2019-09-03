package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.stn.ester.rest.domain.constant.EntityConstant;
import com.stn.ester.rest.domain.enumerate.CityStatus;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class City extends AppDomain {
    public static final String unique_name = "city";
    private static final String COLUMN_STATE = "state_id";
    private static final String JSON_PROPERTY_STATE = "stateId";

    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
    @Column(nullable = false, unique = true)
    private String name;

    private int postal_code;

    @Enumerated(EnumType.STRING)
    private CityStatus cityStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_STATE, insertable = false, updatable = false)
    private State state;

    @JsonProperty(JSON_PROPERTY_STATE)
    @Column(name = COLUMN_STATE)
    private Long stateId;

    @JsonSetter(JSON_PROPERTY_STATE)
    public void setStateId(long stateId) {
        if (stateId != 0)
            this.stateId = stateId;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String underscoreName() {
        return unique_name;
    }
}
