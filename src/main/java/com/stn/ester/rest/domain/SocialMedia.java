package com.stn.ester.rest.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class SocialMedia extends AppDomain {
    public static String unique_name = "social_media";

    @NotBlank(message = "Name is mandatory.")
    @Column(nullable = false)
    private String name;

    private String url;
    private int orderingNumber;

    public SocialMedia() {

    }

    public String underscoreName() {
        return unique_name;
    }
}
