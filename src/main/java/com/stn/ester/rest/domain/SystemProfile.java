package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class SystemProfile extends AppDomain {
    private static final String COLUMN_LOGO_ID = "logo_id";
    private static final String COLUMN_CITY_ID = "city_id";
    private static final String COLUMN_COUNTRY_ID = "country_id";
    private static final String COLUMN_IMAGE_BACKGROUND_ID = "image_background_id";
    private static final String MAPPED_BY_SYSTEM_PROFILE = "systemProfile";
    private static final String JSON_PROPERTY_ASSET_FILE_ID = "assetFileId";
    private static final String JSON_PROPERTY_IMAGE_BACKGROUND_ID = "imageBackgroundId";
    private static final String COLUMN_DEFINITION_TEXT = "TEXT";
    private static final String VALIDATOR_EMAIL_MESSAGE = "Invalid Email Format.";
    private static final String VALIDATOR_URL_MESSAGE = "Invalid URL Website.";
    private static final String VALIDATOR_URL_REGEXP = "^(http|https).*";

    public static final String unique_name = "system_profile";

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_LOGO_ID, insertable = false, updatable = false)
    private AssetFile assetFile;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = MAPPED_BY_SYSTEM_PROFILE ,cascade = CascadeType.ALL)
    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    private Set<SocialMedia> socialMedia =  new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_CITY_ID, insertable = false, updatable = false)
    private City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_COUNTRY_ID, insertable = false, updatable = false)
    private Country country;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_IMAGE_BACKGROUND_ID, insertable = false, updatable = false)
    private AssetFile imageBackground;

    @JsonProperty(JSON_PROPERTY_ASSET_FILE_ID)
    @Column(name = COLUMN_LOGO_ID)
    private Long assetFileId;

    @JsonProperty(JSON_PROPERTY_IMAGE_BACKGROUND_ID)
    @Column(name = COLUMN_IMAGE_BACKGROUND_ID)
    private Long imageBackgroundId;

    public void setAssetFileId(Long assetFileId) {
        if (assetFileId != null)
            this.assetFileId = assetFileId;
    }

    @Transient
    private String tokenLogo;
    @Transient
    private String tokenImageBackground;
    private String address;
    private String telephone;
    private String name;
    private String shortname;
    @Column(columnDefinition = COLUMN_DEFINITION_TEXT)
    private String header;
    @Email(message = VALIDATOR_EMAIL_MESSAGE)
    private String email;
    @URL(regexp = VALIDATOR_URL_REGEXP, message = VALIDATOR_URL_MESSAGE)
    private String website;
    private String appName;
    private String appShortName;
    private String companyShortName;
    private String companyName;
    private Date startYear;

    public SystemProfile() {

    }

    public SystemProfile(String address, String telephone, String name, String shortname, String header, String email, String website, Long assetFileId) {
        this.address = address;
        this.telephone = telephone;
        this.name = name;
        this.shortname = shortname;
        this.header = header;
        this.email = email;
        this.website = website;
        this.assetFileId = assetFileId;
    }

    public String getTokenLogo() {
        return this.tokenLogo;
    }

    public String getTokenImageBackground() {
        return this.tokenImageBackground;
    }

    public String getAddress() {
        return this.address;
    }

    public String getName() {
        return this.name;
    }

    public String getWebsite() {
        return this.website;
    }

    @Override
    public String underscoreName() {
        return unique_name;
    }
}
