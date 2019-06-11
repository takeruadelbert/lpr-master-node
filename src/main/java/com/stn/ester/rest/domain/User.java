package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@Entity
public class User extends AppDomain {

    public static final String unique_name = "user";

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must be Alphanumeric.")
    @NotBlank(message = "Username is mandatory.")
    @Column(nullable = false, unique = true)
    private String username;

    @Email(message = "Invalid Email Format.")
    @NotBlank(message = "Email is mandatory.")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password is mandatory.")
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

    @NotNull(message = "User Group is mandatory.")
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
