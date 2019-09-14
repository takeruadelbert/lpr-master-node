package com.stn.ester.rest.domain;

import com.stn.ester.rest.domain.constant.EntityConstant;
import com.stn.ester.rest.helper.GlobalFunctionHelper;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
public class AssetFile extends AppDomain {
    public static final String unique_name = "asset_file";

    private String path;
    private String name;
    private String extension;
    private String token;
    @Column(columnDefinition = EntityConstant.COLUMN_DEFAULT_INIT_ZERO, nullable = false)
    private int isDefault;

    public AssetFile() {

    }

    public AssetFile(String path, String name, String extension) {
        this.path = path;
        this.name = name;
        this.extension = extension;
        this.token = GlobalFunctionHelper.generateToken();
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

    public void setAssetFileToDefault() {
        this.isDefault = 1;
    }

    public int getIsDefault() {
        return this.isDefault;
    }

    @Override
    public String underscoreName() {
        return unique_name;
    }
}
