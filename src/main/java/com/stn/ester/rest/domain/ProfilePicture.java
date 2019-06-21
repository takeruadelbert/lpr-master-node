package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Data
@Entity
public class ProfilePicture extends AppDomain {
    public static final String unique_name = "profile_picture";

    @Column(columnDefinition = "MEDIUMTEXT")
    public String avatar;

    @Override
    public String underscoreName() { return null; }
}
