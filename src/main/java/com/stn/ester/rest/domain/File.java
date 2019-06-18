package com.stn.ester.rest.domain;

import javax.persistence.*;

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

    @Column(columnDefinition = "TEXT")
    public String file;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public File(String imagePath) {
        super();
    }

    @Override
    public String underscoreName() {
        return null;
    }
}
