package com.stn.ester.controllers.crud;

import com.stn.ester.core.base.AccessAllowed;
import com.stn.ester.services.crud.AssetFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/asset_files")
public class AssetFileController {
    private AssetFileService assetFileService;

    @Autowired
    public AssetFileController(AssetFileService assetFileService) {
        this.assetFileService = assetFileService;
    }

    @PreAuthorize("hasPermission(null,'allowall')")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Object uploadFile(@RequestParam MultipartFile[] files) {
        return assetFileService.uploadFile(files);
    }

    @PreAuthorize("hasPermission(null,'allowall')")
    @RequestMapping(value = "/upload-encoded", method = RequestMethod.POST)
    public Object uploadEncodedFile(@RequestParam String filename, @RequestParam String files) {
        return assetFileService.uploadEncodedFile(filename, files);
    }

    @AccessAllowed
    @RequestMapping(value = "/{token}", method = RequestMethod.GET)
    @NotNull
    public Object getFile(@PathVariable String token, @RequestParam(value = "dl", required = false) Integer flag_dl) {
        boolean is_dl = false;
        if (flag_dl != null) {
            is_dl = flag_dl == 1 ? true : false;
        }
        return assetFileService.getFile(token, is_dl);
    }
}