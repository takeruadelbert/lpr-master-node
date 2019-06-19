package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_status_id", insertable = false, updatable = false)
    private CityStatus cityStatus;

    @JsonProperty("cityStatusId")
    @Column(name = "city_status_id")
    private Long cityStatusId;

    @JsonSetter("cityStatusId")
    public void setCityStatusId(long cityStatusId) {
        if (cityStatusId != 0)
            this.cityStatusId = cityStatusId;
    }

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
