package com.stn.ester.rest.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
@Entity
public class LoginSession extends AppDomain {

    private String token;

    private Date expire;

    @ManyToOne
    private User user;

    public LoginSession(String token, Date expire, User user) {
        this.token = token;
        this.expire = expire;
        this.user = user;
    }

    @Override
    public String underscoreName() {
        return "login_session";
    }
}
