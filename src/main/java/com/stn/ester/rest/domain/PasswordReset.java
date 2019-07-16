package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
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
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    @JsonBackReference
    private User user;

    @JsonProperty("userId")
    @Column(name = "user_id")
    private Long userId;

    public PasswordReset() {

    }

    @JsonSetter("userId")
    public void setUserId(long userId) {
        if (userId != 0)
            this.userId = userId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public int getIs_used() {
        return is_used;
    }

    public void setIs_used(int is_used) {
        if (is_used == 0) {
            this.is_used = 1;
        }
    }

    @Override
    public String underscoreName() {return PasswordReset.unique_name;}
}
