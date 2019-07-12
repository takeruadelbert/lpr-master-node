package com.stn.ester.rest.domain;

import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;

@Data
@Entity
public class PasswordReset extends AppDomain {
    public static final String unique_name = "password_reset";

    private String token;
    private int is_used;
    private Date expire;

    @Override
    public String underscoreName() {return PasswordReset.unique_name;}
}
