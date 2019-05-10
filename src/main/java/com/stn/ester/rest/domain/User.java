package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class User extends AppDomain {

    public static final String unique_name = "user";

    @Column(nullable = false, unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Biodata biodata;

    //contoh belongsto(unidirection manytoone)
    //start
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_group_id", insertable = false, updatable = false)
    private UserGroup userGroup;

    @JsonProperty("userGroupId")
    @Column(name = "user_group_id")
    private Long userGroupId;

    @JsonSetter("userGroupId")
    public void setUserGroupId(long userGroupId) {
        if (userGroupId != 0)
            this.userGroupId = userGroupId;
    }
    //end

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserGroupId() {
        return userGroupId;
    }

    @Override
    public String underscoreName() {
        return User.unique_name;
    }
}
