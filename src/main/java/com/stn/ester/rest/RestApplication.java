package com.stn.ester.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stn.ester.rest.dao.jpa.AssetFileRepository;
import com.stn.ester.rest.domain.*;
import com.stn.ester.rest.domain.enumerate.RequestMethod;
import com.stn.ester.rest.helper.GlobalFunctionHelper;
import com.stn.ester.rest.service.MenuService;
import com.stn.ester.rest.service.ModuleService;
import com.stn.ester.rest.service.UserGroupService;
import com.stn.ester.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.TimeZone;

@SpringBootApplication
public class RestApplication extends SpringBootServletInitializer {

    @Value("${ester.server.timezone}")
    private String timezone;
    private String parentDirectory = new File(System.getProperty("user.dir")).getParent() != null ? new File(System.getProperty("user.dir")).getParent() : new File(System.getProperty("user.dir")).toString();
    @Value("${ester.asset.root}")
    private String assetPath;
    @Value("${ester.asset.default}")
    private String assetDefault;
    private static final String DS = File.separator;

    @Autowired
    private AssetFileRepository assetFileRepository;

    @Autowired
    private UserGroupService userGroupService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;

    @Autowired
    private MenuService menuService;

    public static Long defaultProfilePictureID;

    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(timezone));
        this.addDefaultProfilePicture();
        this.addSuperAdmin();
        try {
            this.addMisc();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(RestApplication.class);
    }

    private void addDefaultProfilePicture() {
        try {
            Optional<AssetFile> existingDefaultPP = this.assetFileRepository.findByNameAndExtension("default-pp", "png");
            if (existingDefaultPP.equals(Optional.empty())) {
                String assetDefaultDir = this.parentDirectory + DS + this.assetPath + DS + this.assetDefault;

                // create asset default dir if it doesn't exist
                GlobalFunctionHelper.autoCreateDir(assetDefaultDir);

                // add default profile picture to Asset File table
                String filename = "default-pp.png";
                String defaultProfilePictPath = DS + this.assetPath + DS + this.assetDefault + DS + filename;
                AssetFile defaultPP = new AssetFile(defaultProfilePictPath, GlobalFunctionHelper.getNameFile(filename), GlobalFunctionHelper.getExtensionFile(filename));
                this.assetFileRepository.save(defaultPP);
                defaultProfilePictureID = defaultPP.getId();
            } else {
                defaultProfilePictureID = existingDefaultPP.get().getId();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addSuperAdmin() {
        UserGroup userGroup = new UserGroup();
        userGroup.setName("SUPERADMIN");
        userGroup.setLabel("Super Admin");
        userGroup = (UserGroup) userGroupService.create(userGroup);
        User user = new User();
        user.setUsername("admin");
        user.setPassword("adminstn");
        user.setEmail("info@suryateknologi.co.id");
        user.setUserGroupId(userGroup.getId());
        Biodata biodata = new Biodata();
        biodata.setFirstName("STN");
        biodata.setLastName("Ester");
        user.setBiodata(biodata);
        userService.create(user);
    }

    public void addMisc() throws IOException {
        ObjectMapper mapper=new ObjectMapper();
        String userModuleData="{\n" +
                "\t\"name\":\"user\",\n" +
                "\t\"alias\":\"/users\",\n" +
                "\t\"requestMethod\":\"GET\",\n" +
                "\t\"moduleLink\":[]\n" +
                "}";
        Module userModule=mapper.readValue(userModuleData,Module.class);
        moduleService.create(userModule);
        String pengaturanMenuData="{\n" +
                "\t\"label\":\"Pengaturan\",\n" +
                "\t\"orderingNumber\":9999\n" +
                "}";
        String userMenuData="{\n" +
                "    \"label\": \"User\",\n" +
                "    \"orderingNumber\": 3,\n" +
                "    \"moduleId\": 1,\n" +
                "    \"parentMenuId\": 1\n" +
                "}";
        Menu pengaturanMenu=mapper.readValue(pengaturanMenuData,Menu.class);
        Menu userMenu=mapper.readValue(userMenuData,Menu.class);
        menuService.create(pengaturanMenu);
        menuService.create(userMenu);
    }
}
