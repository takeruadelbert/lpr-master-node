package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.stn.ester.rest.domain.enumerate.CityStatus;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class City extends AppDomain {
    public static final String unique_name = "city";

    @NotBlank(message = "Name is mandatory.")
    @Column(nullable = false, unique = true)
    private String name;

    private int postal_code;

    @Enumerated(EnumType.STRING)
    private CityStatus cityStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id", insertable = false, updatable = false)
    private State state;

    @JsonProperty("stateId")
    @Column(name = "state_id")
    private Long stateId;

    @JsonSetter("stateId")
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
