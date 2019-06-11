package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Biodata extends AppDomain{

    public static final String unique_name="biodata";

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = false)
    private String identityNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gender_id", insertable = false, updatable = false)
    private Gender gender;

    @JsonProperty("genderId")
    @Column(name = "gender_id")
    private Long genderId;

    @JsonSetter("genderId")
    public void setGenderId(long genderId) {
        if (genderId != 0)
            this.genderId = genderId;
    }

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

    @Override
    public String underscoreName() {
        return Biodata.unique_name;
    }
}
