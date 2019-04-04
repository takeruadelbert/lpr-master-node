package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class User extends AppDomain{

    @Column(nullable = false,unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Biodata biodata;

    @Override
    public String underscoreName() {
        return "user";
    }
}
