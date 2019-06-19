package com.stn.ester.rest.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

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
public class File extends AppDomain {
    public static final String unique_name = "file";

    public String url;

    public String name;

    public String extension;

    @Column(columnDefinition = "MEDIUMTEXT")
    public String base64Image;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @OneToOne
    @JoinColumn(name = "system_profile_id")
    @JsonBackReference
    private SystemProfile systemProfile;

    @Override
    public String underscoreName() {
        return null;
    }
}
