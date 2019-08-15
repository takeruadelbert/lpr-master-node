package com.stn.ester.rest.service.base;

import com.stn.ester.rest.domain.AssetFile;

public interface AssetFileBehaviour {
    String getAssetPath();

    AssetFile claimFile(String fileToken);
}
