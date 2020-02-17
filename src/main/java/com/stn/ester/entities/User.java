package com.stn.ester.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.stn.ester.core.base.AutoRemoveChild;
import com.stn.ester.core.base.AutoRemoveChildType;
import com.stn.ester.core.base.TableFieldPair;
import com.stn.ester.core.validation.IsUnique;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.constant.EntityConstant;
import com.stn.ester.entities.enumerate.UserStatus;
import com.stn.ester.entities.validationgroups.Create;
import com.stn.ester.entities.validationgroups.Update;
import com.stn.ester.repositories.jpa.RoleGroupRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import static com.stn.ester.core.security.SecurityConstants.ROLE_PREFIX;

@Data
@Entity
@IsUnique.List(value = {
        @IsUnique(columnNames = "username", message = "Username have been taken", groups = {Create.class, Update.class}),
        @IsUnique(columnNames = "email", message = "Email have been taken", groups = {Create.class, Update.class})
})
@AutoRemoveChild({
        @TableFieldPair(attributeName = "roleGroups", fieldName = "roleId", attributeArrayName = "roleIds", repository = RoleGroupRepository.class, autoRemoveChildType = AutoRemoveChildType.MANYTOMANY)
})
public class User extends BaseEntity implements UserDetails {

    private static final String COLUMN_PROFILE_PICTURE_ID = "profile_picture_id";
    private static final String COLUMN_MAPPED_USER = "user";
    private static final String COLUMN_EMPLOYEE_ID = "employee_id";

    private static final String JSON_PROPERTY_PROFILE_PICTURE = "profilePictureId";
    private static final String DEFINITION_COLUMN_USER_STATUS = "varchar(255) default 'ACTIVE'";

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = EntityConstant.MESSAGE_ALPHANUMERIC_ONLY, groups = {Create.class, Update.class})
    @NotBlank(groups = Create.class, message = EntityConstant.MESSAGE_NOT_BLANK)
    @Column(nullable = false, unique = true)
    private String username;

    @Email(message = EntityConstant.MESSAGE_INVALID_FORMAT, groups = {Create.class, Update.class})
    @NotBlank(groups = {Create.class, Update.class}, message = EntityConstant.MESSAGE_NOT_BLANK)
    @Column(unique = true)
    private String email;

    @NotBlank(groups = Create.class, message = EntityConstant.MESSAGE_NOT_BLANK)
    @Null(groups = Update.class)
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

    @OneToOne(fetch = FetchType.LAZY, mappedBy = COLUMN_MAPPED_USER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private PasswordReset passwordReset;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<RoleGroup> roleGroups;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Collection<Long> roleIds;

    @Column(columnDefinition = DEFINITION_COLUMN_USER_STATUS)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.ACTIVE;

    @Transient
    private String token;

    private LocalDateTime lastLogin;

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
        return userStatus.equals(UserStatus.ACTIVE);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList();
        if (this.roleGroups != null) {
            for (RoleGroup roleGroup : roleGroups) {
                authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + "_" + roleGroup.getRole().getName()));
            }
        }
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

    public String getToken() {
        return this.token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBiodata(Biodata biodata) {
        this.biodata = biodata;
    }

    public void addRole(Role role) {
        if (this.roleGroups == null) {
            this.roleGroups = new ArrayList<>();
        }
        RoleGroup roleGroup = new RoleGroup();
        roleGroup.setUser(this);
        roleGroup.setRole(role);
        this.roleGroups.add(roleGroup);
    }

}
