package com.stn.ester.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.constant.EntityConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class SocialMedia extends BaseEntity {
    private static final String COLUMN_SYSTEM_PROFILE = "system_profile_id";
    private static final String JSON_PROPERTY_SYSTEM_PROFILE = "systemProfileId";
    private static final String VALIDATOR_URL_MESSAGE = "Invalid URL.";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_SYSTEM_PROFILE, insertable = false, updatable = false)
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    private SystemProfile systemProfile;

    @JsonProperty(JSON_PROPERTY_SYSTEM_PROFILE)
    @Column(name = COLUMN_SYSTEM_PROFILE)
    private Long systemProfileId;

    @URL(message = VALIDATOR_URL_MESSAGE)
    @NotBlank(message = EntityConstant.MESSAGE_NOT_BLANK)
    @Column(nullable = false)
    private String url;
    private String name;
    private int orderingNumber;

    public SocialMedia() {

    }

}
