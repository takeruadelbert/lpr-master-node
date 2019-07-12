package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.Date;

@Data
@Entity
public class PasswordReset extends AppDomain {
    public static final String unique_name = "password_reset";

    private String token;
    private int is_used;
    private Date expire;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Override
    public String underscoreName() {return PasswordReset.unique_name;}
}
