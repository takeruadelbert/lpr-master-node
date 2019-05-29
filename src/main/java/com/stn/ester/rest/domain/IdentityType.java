package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.stn.ester.rest.controller.AppController;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.*;

@Data
@Entity
public class IdentityType extends AppDomain {

    public static final String unique_name = "identity_type";

    public String label;

    @Column(nullable = false)
    private String name;

    @Override
    public String underscoreName() {
        return null;
    }
}
