package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import static com.stn.ester.rest.security.SecurityConstants.*;

@Data
@Entity
public class SocialMedia extends AppDomain {
    public static String unique_name = "social_media";

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = SYSTEM_PROFILE_ID_COLUMN, insertable = false, updatable = false)
    @JsonBackReference
    private SystemProfile systemProfile;

    @JsonProperty("systemProfileId")
    @Column(name = SYSTEM_PROFILE_ID_COLUMN)
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
