package com.stn.ester.services.crud;

import com.stn.ester.entities.AssetFile;
import com.stn.ester.entities.SystemProfile;
import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.repositories.jpa.AssetFileRepository;
import com.stn.ester.repositories.jpa.SystemProfileRepository;
import com.stn.ester.services.base.CrudService;
import com.stn.ester.services.base.traits.AssetFileClaimTrait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class SystemProfileService extends CrudService implements AssetFileClaimTrait {
    private SystemProfileRepository systemProfileRepository;
    private AssetFileService assetFileService;
    private AssetFileRepository assetFileRepository;
    private String asset_path = "system_profile";
    private final String logoType = "logo";
    private final String backgroundImageType = "bg-image";

    @Autowired
    public SystemProfileService(SystemProfileRepository systemProfileRepository, AssetFileService assetFileService, AssetFileRepository assetFileRepository) {
        super(SystemProfile.class, systemProfileRepository);
        super.repositories.put(SystemProfile.class.getName(), systemProfileRepository);
        this.systemProfileRepository = systemProfileRepository;
        this.assetFileService = assetFileService;
        this.assetFileRepository = assetFileRepository;
    }

    @Transactional
    public Object updateSingleData(BaseEntity object) {
        Long id = Long.valueOf(1);
        SystemProfile systemProfile = ((SystemProfile) object);

        // insert asset file ID if there's any file uploaded.
        String tokenLogo = systemProfile.getTokenLogo();
        if (tokenLogo != null && !tokenLogo.isEmpty()) {
            AssetFile logo = this.claimFile(tokenLogo);
            systemProfile.setLogoId(logo.getId());
            systemProfile.setLogo(logo);
        }

        // insert image background ID if there's any file uploaded.
        String tokenImageBackground = systemProfile.getTokenImageBackground();
        if (tokenImageBackground != null && !tokenImageBackground.isEmpty()) {
            AssetFile imageBackground = this.claimFile(tokenImageBackground);
            systemProfile.setImageBackgroundId(imageBackground.getId());
            systemProfile.setImageBackground(imageBackground);
        }

        if (systemProfileRepository.existsById(id)) {
            systemProfile.setId(id);
            return super.update(id, systemProfile);
        } else {
            return super.create(systemProfile);
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
        return getDataSystemProfile(logoType);
    }

    public BufferedImage getBackgroundImage() throws IOException {
        return getDataSystemProfile(backgroundImageType);
    }

    private BufferedImage getDataSystemProfile(String type) throws IOException {
        BufferedImage bufferedImage = null;
        SystemProfile systemProfile = this.systemProfileRepository.findFirstByIdIsNotNull();
        if (systemProfile != null) {
            Long assetFileId = type.equals(logoType) ? systemProfile.getLogoId() : systemProfile.getImageBackgroundId();
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
