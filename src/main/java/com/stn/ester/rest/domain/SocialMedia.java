package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class SocialMedia extends AppDomain {
    public static String unique_name = "social_media";

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "system_profile_id", insertable = false, updatable = false)
    @JsonBackReference
    private SystemProfile systemProfile;

    @JsonProperty("systemProfileId")
    @Column(name = "system_profile_id")
    private Long systemProfileId;

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
