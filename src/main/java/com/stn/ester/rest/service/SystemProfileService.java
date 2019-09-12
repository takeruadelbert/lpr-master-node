package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.AssetFileRepository;
import com.stn.ester.rest.dao.jpa.SystemProfileRepository;
import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.domain.AssetFile;
import com.stn.ester.rest.domain.SystemProfile;
import com.stn.ester.rest.service.base.AssetFileBehaviour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class SystemProfileService extends AppService implements AssetFileBehaviour {
    private SystemProfileRepository systemProfileRepository;
    private AssetFileService assetFileService;
    private AssetFileRepository assetFileRepository;
    private String asset_path = "system_profile";

    @Autowired
    public SystemProfileService(SystemProfileRepository systemProfileRepository, AssetFileService assetFileService, AssetFileRepository assetFileRepository) {
        super(SystemProfile.unique_name);
        super.repositories.put(SystemProfile.unique_name, systemProfileRepository);
        this.systemProfileRepository = systemProfileRepository;
        this.assetFileService = assetFileService;
        this.assetFileRepository = assetFileRepository;
    }

    @Transactional
    public Object updateSingleData(AppDomain object) {
        Long id = Long.valueOf(1);

        // insert asset file ID if there's any file uploaded.
        String tokenLogo = ((SystemProfile) object).getTokenLogo();
        if (tokenLogo != null) {
            ((SystemProfile) object).setAssetFileId(this.claimFile(tokenLogo).getId());
        }
        // insert image background ID if there's any file uploaded.
        String tokenImageBackground = ((SystemProfile) object).getTokenImageBackground();
        if (tokenImageBackground != null) {
            ((SystemProfile) object).setImageBackgroundId(this.claimFile(tokenImageBackground).getId());
        }
        if (systemProfileRepository.existsById(id)) {
            object.setId(id);
            return super.update(id, object);
        } else {
            return super.create(object);
        }
    }

    @Override
    public String getAssetPath() {
        return this.asset_path;
    }

    @Override
    public AssetFile claimFile(String fileToken) {
        return this.assetFileService.moveTempDirToPermanentDir(fileToken, this.getAssetPath());
    }

    public SystemProfile get() {
        return this.systemProfileRepository.findById(1L).get();
    }

    public BufferedImage getLogoImage() throws IOException {
        return getDataSystemProfile("logo");
    }

    public BufferedImage getBackgroundImage() throws IOException {
        return getDataSystemProfile("background-image");
    }

    private BufferedImage getDataSystemProfile(String type) throws IOException {
        BufferedImage bufferedImage = null;
        SystemProfile systemProfile = this.systemProfileRepository.findFirstByIdIsNotNull();
        if (systemProfile != null) {
            Long assetFileId = type.toLowerCase() == "logo" ? systemProfile.getAssetFileId() : systemProfile.getImageBackgroundId();
            if (assetFileId != null) {
                AssetFile assetFile = this.assetFileRepository.findById(assetFileId).get();
                if (assetFile != null) {
                    String token = assetFile.getToken();
                    if (token != null && !token.isEmpty()) {
                        byte[] data = this.assetFileService.getFile(token);
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
                        bufferedImage = ImageIO.read(byteArrayInputStream);
                    }
                }
            }
        }
        return bufferedImage;
    }
}
