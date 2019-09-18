package com.stn.ester.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.constant.EntityConstant;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class PasswordReset extends BaseEntity {
    private static final String COLUMN_USER = "user_id";
    private static final String JSON_PROPERTY_USER = "userId";

    private String token;

    @Column(columnDefinition = EntityConstant.COLUMN_DEFAULT_INIT_ZERO, nullable = false)
    private int isUsed;
    private Date expire;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_USER, insertable = false, updatable = false)
    @JsonBackReference
    private User user;

    @JsonProperty(JSON_PROPERTY_USER)
    @Column(name = COLUMN_USER)
    private Long userId;

    public PasswordReset() {

    }

    @JsonSetter(JSON_PROPERTY_USER)
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

    public int getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    public Long getUserId() {
        return userId;
    }

}
