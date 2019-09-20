package com.stn.ester.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.constant.EntityConstant;
import com.stn.ester.entities.enumerate.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@EqualsAndHashCode(exclude = {"user"})
@ToString(exclude = {"user"})
public class Biodata extends BaseEntity {

    private static final String COLUMN_USER = "user_id";
    private static final String COLUMN_IDENTITY_TYPE = "identity_type_id";
    private static final String COLUMN_COUNTRY = "country_id";
    private static final String COLUMN_STATE = "state_id";
    private static final String COLUMN_CITY = "city_id";
    private static final String JSON_PROPERTY_COUNTRY = "countryId";
    private static final String JSON_PROPERTY_STATE = "stateId";
    private static final String JSON_PROPERTY_CITY = "cityId";
    private static final String JSON_PROPERTY_IDENTITY_TYPE = "identityTypeId";

    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_USER)
    @JsonBackReference
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_IDENTITY_TYPE, insertable = false, updatable = false)
    private IdentityType identityType;

    @JsonProperty(JSON_PROPERTY_IDENTITY_TYPE)
    @Column(name = COLUMN_IDENTITY_TYPE)
    private Long identityTypeId;

    @JsonSetter(JSON_PROPERTY_IDENTITY_TYPE)
    public void setIdentityTypeId(long identityTypeId) {
        if (identityTypeId != 0)
            this.identityTypeId = identityTypeId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_COUNTRY, insertable = false, updatable = false)
    private Country country;

    @JsonProperty(JSON_PROPERTY_COUNTRY)
    @Column(name = COLUMN_COUNTRY)
    private Long countryId;

    @JsonSetter(JSON_PROPERTY_COUNTRY)
    public void setCountryId(long countryId) {
        if (countryId != 0)
            this.countryId = countryId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_STATE, insertable = false, updatable = false)
    private State state;

    @JsonProperty(JSON_PROPERTY_STATE)
    @Column(name = COLUMN_STATE)
    private Long stateId;

    public void setStateId(long stateId) {
        if (stateId != 0)
            this.stateId = stateId;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_CITY, insertable = false, updatable = false)
    private City city;

    @JsonProperty(JSON_PROPERTY_CITY)
    @Column(name = COLUMN_CITY)
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

    public String getFullname() {
        return String.format("%s %s", this.firstName, this.lastName == null ? "" : this.lastName).trim();
    }
}
