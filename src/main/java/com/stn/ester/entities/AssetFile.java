package com.stn.ester.entities;

import com.stn.ester.entities.base.BaseEntity;
import com.stn.ester.entities.constant.EntityConstant;
import com.stn.ester.helpers.GlobalFunctionHelper;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity
public class AssetFile extends BaseEntity {

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
}
