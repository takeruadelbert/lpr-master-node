package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
public class User{

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false,unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user",cascade = CascadeType.ALL)
    @JsonManagedReference
    private Biodata biodata;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Biodata getBiodata() {
        return biodata;
    }

    public void setBiodata(Biodata biodata) {
        this.biodata = biodata;
    }
}
