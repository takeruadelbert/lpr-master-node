package com.stn.ester.rest.controller;

import com.stn.ester.rest.service.AssetFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/asset_files")
public class AssetFileController {
    private AssetFileService assetFileService;

    @Autowired
    public AssetFileController(AssetFileService assetFileService) {
        this.assetFileService = assetFileService;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Object uploadFile(@RequestParam MultipartFile[] files) {
        return assetFileService.uploadFile(files);
    }

    @RequestMapping(value = "/upload-encoded", method = RequestMethod.POST)
    public Object uploadEncodedFile(@RequestParam String filename, @RequestParam String files) {
        return assetFileService.uploadEncodedFile(filename, files);
    }

    @RequestMapping(value = "/{token}", method = RequestMethod.GET)
    @NotNull
    public Object getFile(@PathVariable String token, @RequestParam(value = "dl", required = false) Integer flag_dl) {
        boolean is_dl = false;
        if (flag_dl != null) {
            is_dl = flag_dl == 1 ? true : false;
        }
        return assetFileService.getFile(token, is_dl);
    }

    private static String extractFilePath(HttpServletRequest request) {
        String path = (String) request.getAttribute(
                HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(
                HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        AntPathMatcher apm = new AntPathMatcher();
        return apm.extractPathWithinPattern(bestMatchPattern, path);
    }
}