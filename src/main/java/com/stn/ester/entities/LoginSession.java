package com.stn.ester.entities;

import com.stn.ester.entities.base.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
@Entity
public class LoginSession extends BaseEntity {

    private String token;

    private Date expire;

    @ManyToOne
    private User user;

    public LoginSession() {
    }

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

}
