package com.stn.ester.rest.domain;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Email;

@Data
@Entity
public class SystemProfile extends AppDomain {
    public static final String unique_name = "system_profile";

    private String logo;
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

    @Override
    public String underscoreName() {
        return unique_name;
    }
}
