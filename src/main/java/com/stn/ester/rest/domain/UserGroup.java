package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
public class UserGroup extends AppDomain {

    public static final String unique_name="user_group";

    private String label;

    @Column(unique = true)
    private String name;

    @Override
    public String underscoreName() {
        return UserGroup.unique_name;
    }
}
