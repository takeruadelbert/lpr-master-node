package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.stn.ester.rest.domain.enumerate.Gender;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class Biodata extends AppDomain {

    public static final String unique_name = "biodata";

    @NotBlank(message = "First Name is mandatory.")
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true)
    private String identityNumber;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "identity_type_id", insertable = false, updatable = false)
    private IdentityType identityType;

    @JsonProperty("identityTypeId")
    @Column(name = "identity_type_id")
    private Long identityTypeId;

    @JsonSetter("identityTypeId")
    public void setIdentityTypeId(long identityTypeId) {
        if (identityTypeId != 0)
            this.identityTypeId = identityTypeId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", insertable = false, updatable = false)
    private Country country;

    @JsonProperty("countryId")
    @Column(name = "country_id")
    private Long countryId;

    @JsonSetter("countryId")
    public void setCountryId(long countryId) {
        if (countryId != 0)
            this.countryId = countryId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "state_id", insertable = false, updatable = false)
    private State state;

    @JsonProperty("stateId")
    @Column(name = "state_id")
    private Long stateId;

    public void setStateId(long stateId) {
        if (stateId != 0)
            this.stateId = stateId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id", insertable = false, updatable = false)
    private City city;

    @JsonProperty("cityId")
    @Column(name = "city_id")
    private Long cityId;

    public void setCityId(long cityId) {
        if (cityId != 0)
            this.cityId = cityId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String underscoreName() {
        return Biodata.unique_name;
    }
}
