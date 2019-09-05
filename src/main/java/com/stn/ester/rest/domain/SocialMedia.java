package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class SocialMedia extends AppDomain {
    private static final String VALIDATOR_URL_MESSAGE = "Invalid URL.";
    private static final String NOT_BLANK_URL_MESSAGE = "Url is mandatory.";
    private static final String COLUMN_SYSTEM_PROFILE_ID = "system_profile_id";
    private static final String JSON_PROPERTY_SYSTEM_PROFILE_ID = "systemProfileId";
    public static String unique_name = "social_media";

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = COLUMN_SYSTEM_PROFILE_ID, insertable = false, updatable = false)
    @JsonBackReference
    private SystemProfile systemProfile;

    @JsonProperty(JSON_PROPERTY_SYSTEM_PROFILE_ID)
    @Column(name = COLUMN_SYSTEM_PROFILE_ID)
    private Long systemProfileId;

    @URL(message = VALIDATOR_URL_MESSAGE)
    @NotBlank(message = NOT_BLANK_URL_MESSAGE)
    @Column(nullable = false)
    private String url;

    private int orderingNumber;

    public SocialMedia() {

    }

    public String underscoreName() {
        return unique_name;
    }
}
