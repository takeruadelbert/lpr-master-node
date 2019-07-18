package com.stn.ester.rest.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
@Entity
public class LoginSession extends AppDomain {

    public static final String unique_name="login_session";

    private String token;

    private Date expire;

    @ManyToOne
    private User user;

    private LoginSession(){}

    public LoginSession(String token, Date expire, User user) {
        this.token = token;
        this.expire = expire;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    @Override
    public String underscoreName() {
        return LoginSession.unique_name;
    }
}
