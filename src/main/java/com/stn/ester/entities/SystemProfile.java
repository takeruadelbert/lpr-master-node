package com.stn.ester.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.constant.EntityConstant;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class SystemProfile extends BaseEntity {
    private static final String COLUMN_LOGO = "logo_id";
    private static final String COLUMN_IMAGE_BACKGROUND = "image_background_id";
    private static final String COLUMN_COMPANY_LOGO = "company_logo_id";
    private static final String COLUMN_APP_ICON = "app_icon_id";
    private static final String COLUMN_SYSTEM_PROFILE = "system_profile_id";
    private static final String COLUMN_COUNTRY = "country_id";
    private static final String COLUMN_STATE = "state_id";
    private static final String COLUMN_CITY = "city_id";
    private static final String JSON_PROPERTY_LOGO = "logoId";
    private static final String JSON_PROPERTY_IMAGE_BACKGROUND = "imageBackgroundId";
    private static final String JSON_PROPERTY_COMPANY_LOGO = "companyLogoId";
    private static final String JSON_PROPERTY_APP_ICON = "appIconId";
    private static final String VALIDATOR_URL_REGEXP = "^(http|https).*";

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_LOGO, insertable = false, updatable = false)
    private AssetFile logo;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_COMPANY_LOGO, insertable = false, updatable = false)
    private AssetFile companyLogo;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_APP_ICON, insertable = false, updatable = false)
    private AssetFile appIcon;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = COLUMN_SYSTEM_PROFILE, insertable = false, updatable = false)
    private List<SocialMedia> socialMedia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_CITY, insertable = false, updatable = false)
    private City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_STATE, insertable = false, updatable = false)
    private State state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_COUNTRY, insertable = false, updatable = false)
    private Country country;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_IMAGE_BACKGROUND, insertable = false, updatable = false)
    private AssetFile imageBackground;

    @JsonProperty(JSON_PROPERTY_LOGO)
    @Column(name = COLUMN_LOGO)
    private Long logoId;

    @JsonProperty(JSON_PROPERTY_IMAGE_BACKGROUND)
    @Column(name = COLUMN_IMAGE_BACKGROUND)
    private Long imageBackgroundId;

    @JsonProperty(JSON_PROPERTY_COMPANY_LOGO)
    @Column(name = COLUMN_COMPANY_LOGO)
    private Long companyLogoId;

    @JsonProperty(JSON_PROPERTY_APP_ICON)
    @Column(name = COLUMN_APP_ICON)
    private Long appIconId;

    public void setLogoId(Long logoId) {
        if (logoId != null)
            this.logoId = logoId;
    }

    public void setLogo(AssetFile logo) {
        this.logo = logo;
    }

    @Transient
    private String tokenLogo;

    @Transient
    private String tokenImageBackground;

    @Transient
    private String tokenAppIcon;

    @Transient
    private String tokenCompanyLogo;

    private String address;
    private String telephone;
    private String name;
    private String shortname;
    @Column(columnDefinition = EntityConstant.COLUMN_DEFINITION_TEXT)
    private String header;
    @Email(message = EntityConstant.MESSAGE_INVALID_FORMAT)
    private String email;
    @URL(regexp = VALIDATOR_URL_REGEXP, message = EntityConstant.MESSAGE_INVALID_FORMAT)
    private String website;
    private String appName;
    private String appShortName;
    private String companyShortName;
    private String companyName;
    private Date startYear;

    @Column(columnDefinition = EntityConstant.COLUMN_DEFINITION_BOOLEAN_TRUE)
    private boolean multipleSession;

    public SystemProfile() {

    }

    public SystemProfile(String address, String telephone, String name, String shortname, String header, String email, String website, Long logoId) {
        this.address = address;
        this.telephone = telephone;
        this.name = name;
        this.shortname = shortname;
        this.header = header;
        this.email = email;
        this.website = website;
        this.logoId = logoId;
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

}
