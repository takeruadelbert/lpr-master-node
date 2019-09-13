package com.stn.ester.entities;

import com.fasterxml.jackson.annotation.*;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.constant.EntityConstant;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Collection;

import static com.stn.ester.etc.security.SecurityConstants.ROLE_PREFIX;

@Data
@Entity
public class User extends BaseEntity implements UserDetails {

    private static final String COLUMN_PROFILE_PICTURE_ID = "profile_picture_id";
    private static final String COLUMN_MAPPED_USER = "user";
    private static final String COLUMN_EMPLOYEE_ID = "employee_id";
    private static final String COLUMN_USER_GROUP_ID = "user_group_id";
    private static final String JSON_PROPERTY_USER_GROUP = "userGroupId";
    private static final String JSON_PROPERTY_PROFILE_PICTURE = "profilePictureId";

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = EntityConstant.MESSAGE_ALPHANUMERIC_ONLY, groups = {New.class, Existing.class})
    @NotBlank(groups = New.class, message = EntityConstant.MESSAGE_NOT_BLANK)
    @Column(nullable = false, unique = true)
    private String username;

    @Email(message = EntityConstant.MESSAGE_INVALID_FORMAT, groups = {New.class, Existing.class})
    @NotBlank(groups = {New.class, Existing.class}, message = EntityConstant.MESSAGE_NOT_BLANK)
    @Column(unique = true)
    private String email;

    @NotBlank(groups = New.class, message = EntityConstant.MESSAGE_NOT_BLANK)
    @Null(groups = Existing.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = COLUMN_MAPPED_USER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Biodata biodata;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_PROFILE_PICTURE_ID, insertable = false, updatable = false)
    private AssetFile profilePicture;

    public void setProfilePicture(AssetFile profilePicture) {
        this.profilePicture = profilePicture;
    }

    @JsonProperty(JSON_PROPERTY_PROFILE_PICTURE)
    @Column(name = COLUMN_PROFILE_PICTURE_ID)
    private Long profilePictureId;

    @JsonSetter(JSON_PROPERTY_PROFILE_PICTURE)
    public void setProfilePictureId(Long profilePictureId) {
        if (profilePictureId != null)
            this.profilePictureId = profilePictureId;
    }

    @OneToOne
    @JoinColumn(name = COLUMN_EMPLOYEE_ID)
    @JsonBackReference
    private Employee employee;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = COLUMN_MAPPED_USER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private PasswordReset passwordReset;

    //contoh belongsto(unidirection manytoone)
    //start
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = COLUMN_USER_GROUP_ID, insertable = false, updatable = false)
    private UserGroup userGroup;

    @NotNull(groups = {New.class, Existing.class}, message = EntityConstant.MESSAGE_NOT_BLANK)
    @JsonProperty(JSON_PROPERTY_USER_GROUP)
    @Column(name = COLUMN_USER_GROUP_ID)
    private Long userGroupId;

    @JsonSetter(JSON_PROPERTY_USER_GROUP)
    public void setUserGroupId(long userGroupId) {
        if (userGroupId != 0)
            this.userGroupId = userGroupId;
    }
    //end

    @Transient
    private String token;

    public void setId() {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList();
        if (this.userGroup != null)
            authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + "_" + this.userGroup.getName()));
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public Long getUserGroupId() {
        return userGroupId;
    }

    public String getToken() {
        return this.token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserGroupId(Long userGroupId) {
        this.userGroupId = userGroupId;
    }

    public void setBiodata(Biodata biodata) {
        this.biodata = biodata;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

}
