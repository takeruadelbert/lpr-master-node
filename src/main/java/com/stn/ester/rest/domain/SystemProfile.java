package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@Data
@Entity
public class SystemProfile extends AppDomain {
    public static final String unique_name = "system_profile";

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "logo_id", insertable = false, updatable = false)
    private AssetFile assetFile;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JoinColumn(name = "system_profile_id", insertable = false, updatable = false)
    private List<SocialMedia> socialMedia;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id", insertable = false, updatable = false)
    private City city;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", insertable = false, updatable = false)
    private Country country;

    @JsonProperty("assetFileId")
    @Column(name = "logo_id")
    private Long assetFileId;

    public void setAssetFileId(Long assetFileId) {
        if (assetFileId != null)
            this.assetFileId = assetFileId;
    }

    @Transient
    private String token;
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

    public String getToken() {
        return this.token;
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
