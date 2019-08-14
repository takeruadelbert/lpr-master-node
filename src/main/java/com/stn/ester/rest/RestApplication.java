package com.stn.ester.rest;

import com.stn.ester.rest.config.DatabaseConfig;
import com.stn.ester.rest.domain.Biodata;
import com.stn.ester.rest.domain.User;
import com.stn.ester.rest.domain.UserGroup;
import com.stn.ester.rest.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

import static com.stn.ester.rest.security.SecurityConstants.ROLE_SUPERADMIN;

@SpringBootApplication
public class RestApplication extends SpringBootServletInitializer {

    @Value("${ester.server.timezone}")
    private String timezone;
    @Autowired
    private AssetFileService assetFileService;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private MenuService menuService;

    public static void main(String[] args) {
        if (args.length > 0) {
            String extJsonFile = args[0];
            if (!extJsonFile.isEmpty()) {
                DatabaseConfig.extJsonFileConfigPath = extJsonFile;
            }
        }
        SpringApplication.run(RestApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(timezone));
        this.addDefaultProfilePicture();
        this.addSuperAdmin();
        this.addDefaultSystemProfile();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(RestApplication.class);
    }

    private void addDefaultProfilePicture() {
        this.assetFileService.addDefaultProfilePicture();
    }

    private void addDefaultSystemProfile() {
        this.assetFileService.addDefaultSystemProfileIfDoesntExist();
    }

    public void addSuperAdmin() {
        UserGroup userGroup = new UserGroup();
        userGroup.setName(ROLE_SUPERADMIN);
        userGroup.setLabel("Super Admin");
        userGroup = (UserGroup) userGroupService.createIfNameNotExist(userGroup, ROLE_SUPERADMIN);
        User user = new User();
        String username = "admin";
        user.setUsername(username);
        user.setPassword("adminstn");
        user.setEmail("info@suryateknologi.co.id");
        user.setUserGroupId(userGroup.getId());
        Biodata biodata = new Biodata();
        biodata.setFirstName("STN");
        biodata.setLastName("Ester");
        user.setBiodata(biodata);
        userService.createIfUsernameNotExist(user, username);
    }
}
