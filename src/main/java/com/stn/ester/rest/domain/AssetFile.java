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
    private String token;

    public AssetFile() {

    }

    public AssetFile(String path, String name, String extension) {
        this.path = path;
        this.name = name;
        this.extension = extension;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String underscoreName() {
        return unique_name;
    }
}
