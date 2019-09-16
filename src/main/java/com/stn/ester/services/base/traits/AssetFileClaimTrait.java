package com.stn.ester.services.base.traits;

import com.stn.ester.entities.AssetFile;

public interface AssetFileClaimTrait {
    String getAssetPath();

    AssetFile claimFile(String fileToken);
}
