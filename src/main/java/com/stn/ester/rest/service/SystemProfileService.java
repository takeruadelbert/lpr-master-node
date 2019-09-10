package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.SystemProfileRepository;
import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.domain.AssetFile;
import com.stn.ester.rest.domain.SystemProfile;
import com.stn.ester.rest.service.base.AssetFileBehaviour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SystemProfileService extends AppService implements AssetFileBehaviour {
    private SystemProfileRepository systemProfileRepository;
    private AssetFileService assetFileService;
    private String asset_path = "system_profile";

    @Autowired
    public SystemProfileService(SystemProfileRepository systemProfileRepository, AssetFileService assetFileService) {
        super(SystemProfile.unique_name);
        super.repositories.put(SystemProfile.unique_name, systemProfileRepository);
        this.systemProfileRepository = systemProfileRepository;
        this.assetFileService = assetFileService;
    }

    @Transactional
    public Object updateSingleData(AppDomain object) {
        Long id = Long.valueOf(1);

        // insert asset file ID if there's any file uploaded.
        String tokenLogo = ((SystemProfile) object).getTokenLogo();
        if (!tokenLogo.isEmpty()) {
            ((SystemProfile) object).setAssetFileId(this.claimFile(tokenLogo).getId());
        }
        // insert image background ID if there's any file uploaded.
        String tokenImageBackground = ((SystemProfile) object).getTokenImageBackground();
        if (!tokenImageBackground.isEmpty()) {
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
}
