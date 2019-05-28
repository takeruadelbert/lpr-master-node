package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import javax.persistence.*;
import java.sql.Blob;

@Data
@Entity
public class Biodata extends AppDomain{

    public static final String unique_name="biodata";

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Integer identity_number;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Integer phone_number;

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
    @JoinColumn(name = "type_identity_id", insertable = false, updatable = false)
    private TypeIdentity typeIdentity;

    @JsonProperty("typeIdentityId")
    @Column(name = "type_identity_id")
    private Long typeIdentityId;

    @JsonSetter("typeIdentityId")
    public void setTypeIdentityId(long typeIdentityId) {
        if (typeIdentityId != 0)
            this.typeIdentityId = typeIdentityId;
    }

    @Override
    public String underscoreName() {
        return Biodata.unique_name;
    }
}
