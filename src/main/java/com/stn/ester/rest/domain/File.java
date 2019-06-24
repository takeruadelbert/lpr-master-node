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
    public int Amount;

//    @OneToOne
//    @JoinColumn(name = "user_id")
//    @JsonBackReference
//    private User user;

//    @OneToOne
//    @JoinColumn(name = "system_profile_id")
//    @JsonBackReference
//    private SystemProfile systemProfile;
//
//    @OneToOne
//    @JoinColumn(name = "news_id")
//    @JsonBackReference
//    private News news;

    @Override
    public String underscoreName() {
        return null;
    }
}
