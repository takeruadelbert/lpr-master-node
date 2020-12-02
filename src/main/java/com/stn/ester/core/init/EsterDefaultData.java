package com.stn.ester.core.init;

import com.stn.ester.entities.Biodata;
import com.stn.ester.entities.Role;
import com.stn.ester.entities.User;
import com.stn.ester.services.crud.AssetFileService;
import com.stn.ester.services.crud.RoleService;
import com.stn.ester.services.crud.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

import static com.stn.ester.core.security.SecurityConstants.ROLE_SUPERADMIN;

@Component
public class EsterDefaultData {

    @Value("${ester.server.timezone}")
    private String timezone;

    @Autowired
    private AssetFileService assetFileService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(timezone));
        this.addDefaultProfilePicture();
        this.addSuperAdmin();
        this.addDefaultSystemProfile();
    }

    private void addDefaultProfilePicture() {
        this.assetFileService.addDefaultProfilePicture();
    }

    private void addDefaultSystemProfile() {
        this.assetFileService.addDefaultSystemProfileIfDoesntExist();
    }

    public void addSuperAdmin() {
        Role role = new Role();
        role.setName(ROLE_SUPERADMIN);
        role.setLabel("Super Admin");
        role = (Role) roleService.createIfNameNotExist(role, ROLE_SUPERADMIN);
        User user = new User();
        String username = "admin";
        user.setUsername(username);
        user.setPassword("adminstn");
        user.setEmail("info@suryateknologi.co.id");
        user.addRole(role);
        Biodata biodata = new Biodata();
        biodata.setFirstName("STN");
        biodata.setLastName("Ester");
        biodata.setUser(user);
        user.setBiodata(biodata);
        userService.createIfUsernameNotExist(user, username);
    }
}
