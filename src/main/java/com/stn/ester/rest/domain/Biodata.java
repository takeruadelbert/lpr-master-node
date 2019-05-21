package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Data
@Entity
public class Biodata extends AppDomain{

    public static final String unique_name="biodata";

    private String firstName;
    private String lastName;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToOne
    @JoinColumn(name = "gender_id")
    @JsonBackReference
    private Gender gender;

    @Override
    public String underscoreName() {
        return Biodata.unique_name;
    }
}
