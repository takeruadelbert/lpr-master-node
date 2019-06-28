package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.AssetFile;
import com.stn.ester.rest.service.AssetFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    public Object uploadFile(@RequestParam MultipartFile[] files) {
        return service.uploadFile(files);
    }

    @RequestMapping(value = "/upload-encoded", method = RequestMethod.POST)
    public Object uploadEncodedFile(@RequestParam String filename, @RequestParam String files) {
        return service.uploadEncodedFile(filename, files);
    }
}