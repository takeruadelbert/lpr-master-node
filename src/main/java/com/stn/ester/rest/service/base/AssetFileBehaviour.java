package com.stn.ester.rest.service.base;

public interface AssetFileBehaviour {
    String getAssetPath();

    Long claimFile(String fileToken);
}
