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
    public static final String COLUMN_LOGO_ID = "logo_id";
    public static final String unique_name = "system_profile";

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_LOGO_ID, insertable = false, updatable = false)
    private AssetFile assetFile;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "systemProfile" ,cascade = CascadeType.ALL)
    @JsonManagedReference
    @EqualsAndHashCode.Exclude
    private Set<SocialMedia> socialMedia =  new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id", insertable = false, updatable = false)
    private City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", insertable = false, updatable = false)
    private Country country;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "image_background_id", insertable = false, updatable = false)
    private AssetFile imageBackground;

    @JsonProperty("assetFileId")
    @Column(name = "logo_id")
    private Long assetFileId;

    @JsonProperty("imageBackgroundId")
    @Column(name = "image_background_id")
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
    @Column(columnDefinition = "TEXT")
    private String header;
    @Email(message = "Invalid Email Format.")
    private String email;
    @URL(regexp = "^(http|https).*", message = "Invalid URL Website")
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
