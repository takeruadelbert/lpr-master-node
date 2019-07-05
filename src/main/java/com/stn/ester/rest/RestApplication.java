package com.stn.ester.rest;

import com.stn.ester.rest.dao.jpa.AssetFileRepository;
import com.stn.ester.rest.domain.AssetFile;
import com.stn.ester.rest.helper.GlobalFunctionHelper;
import org.omg.CORBA.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Path;
import java.util.TimeZone;

@SpringBootApplication
public class RestApplication extends SpringBootServletInitializer {

    @Value("${ester.server.timezone}")
    private String timezone;
    private String parentDirectory = new File(System.getProperty("user.dir")).getParent();
    @Value("${ester.asset.root}")
    private String assetPath;
    @Value("${ester.asset.default}")
    private String assetDefault;
    private static final String DS = File.separator;

    @Autowired
    private AssetFileRepository assetFileRepository;

    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(timezone));
        this.addDefaultProfilePicture();
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(RestApplication.class);
    }

    private void addDefaultProfilePicture() {
        try {
            String assetDefaultDir = this.parentDirectory + DS + this.assetPath + DS + this.assetDefault;

            // create asset default dir if it doesn't exist
            GlobalFunctionHelper.autoCreateDir(assetDefaultDir);

            // add default profile picture to Asset File table
            String filename = "default-pp.png";
            String defaultProfilePictPath = DS + this.assetPath + DS + this.assetDefault + DS + filename;
            AssetFile defaultPP = new AssetFile(defaultProfilePictPath, GlobalFunctionHelper.getNameFile(filename), GlobalFunctionHelper.getExtensionFile(filename));
            this.assetFileRepository.save(defaultPP);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
