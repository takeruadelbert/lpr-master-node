package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class PasswordReset extends AppDomain {
    public static final String unique_name = "password_reset";

    private String token;

    @Column(columnDefinition = "tinyint default 0")
    private int is_used;
    private Date expire;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public PasswordReset(String token, Date expire) {
        this.token = token;
        this.expire = expire;
    }

    @Override
    public String underscoreName() {return PasswordReset.unique_name;}
}
