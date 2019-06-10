package com.stn.ester.rest.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.ReadOnlyProperty;

@Data
@Entity
public class Gender extends AppDomain {
    public static final String unique_name = "gender";

    @NotBlank(message = "Label is mandatory.")
    public String label;

    @NotBlank(message = "Name is mandatory.")
    @Column(nullable = false)
    private String name;

    @Override
    public String underscoreName() {
        return null;
    }

}