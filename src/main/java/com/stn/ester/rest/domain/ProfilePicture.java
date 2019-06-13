package com.stn.ester.rest.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
public class ProfilePicture extends AppDomain {
    public static final String unique_name = "profile_picture";

    @Column(columnDefinition = "TEXT")
    public String avatar;

    @Override
    public String underscoreName() { return null; }
}
