package com.stn.ester.rest.domain;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
public class AssetFile extends AppDomain {
    public static final String unique_name = "asset_file";

    private String path;
    private String name;
    private String extension;

    public AssetFile() {

    }

    public AssetFile(String path, String name, String extension) {
        this.path = path;
        this.name = name;
        this.extension = extension;
    }

    @Override
    public String underscoreName() {
        return unique_name;
    }
}
