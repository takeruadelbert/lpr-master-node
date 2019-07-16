package com.stn.ester.rest.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Entity
public class User extends AppDomain implements UserDetails {

    public static final String unique_name = "user";

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must be Alphanumeric.", groups = {New.class, Existing.class})
    @NotBlank(groups = New.class, message = "Username is mandatory.")
    @Column(nullable = false, unique = true)
    private String username;

    @Email(message = "Invalid Email Format.", groups = {New.class, Existing.class})
    @NotBlank(groups = {New.class, Existing.class}, message = "Email is mandatory.")
    @Column(unique = true)
    private String email;

    @NotBlank(groups = New.class, message = "Password is mandatory.")
    @Null(groups = Existing.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Biodata biodata;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_picture_id", insertable = false, updatable = false)
    private AssetFile assetFile;

    @JsonProperty("assetFileId")
    @Column(name = "profile_picture_id")
    private Long assetFileId;

    @JsonSetter("assetFileId")
    public void setAssetFileId(Long assetFileId) {
        if (assetFileId != null)
            this.assetFileId = assetFileId;
    }

    @OneToOne
    @JoinColumn(name = "employee_id")
    @JsonBackReference
    private Employee employee;

    //contoh belongsto(unidirection manytoone)
    //start
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_group_id", insertable = false, updatable = false)
    private UserGroup userGroup;

    @NotNull(groups = {New.class, Existing.class}, message = "User Group is mandatory.")
    @JsonProperty("userGroupId")
    @Column(name = "user_group_id")
    private Long userGroupId;

    @JsonSetter("userGroupId")
    public void setUserGroupId(long userGroupId) {
        if (userGroupId != 0)
            this.userGroupId = userGroupId;
    }
    //end

    @Transient
    private String token;

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities=new ArrayList();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.userGroup.getName()));
        return authorities;
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

    @Override
    public String underscoreName() {
        return User.unique_name;
    }
}
