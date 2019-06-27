package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.AssetFile;
import com.stn.ester.rest.service.AssetFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/asset_files")
public class AssetFileController extends AppController<AssetFileService, AssetFile> {

    @Autowired
    public AssetFileController(AssetFileService assetFileService) {
        super(assetFileService);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Object uploadFile(@RequestParam("file") MultipartFile[] files) {
        return service.uploadFile(files);
    }
}