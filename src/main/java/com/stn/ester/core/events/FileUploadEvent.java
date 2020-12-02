package com.stn.ester.core.events;

import com.stn.ester.entities.AssetFile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class FileUploadEvent extends ApplicationEvent {

    private AssetFile assetFile;
    private String target;

    public FileUploadEvent(Object source, AssetFile assetFile, String target) {
        super(source);
        this.assetFile = assetFile;
        this.target = target;
    }
}
