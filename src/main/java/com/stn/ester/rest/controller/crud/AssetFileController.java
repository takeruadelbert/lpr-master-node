package com.stn.ester.rest.controller.crud;

import com.stn.ester.rest.controller.base.CrudController;
import com.stn.ester.rest.domain.AssetFile;
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
public class AssetFileController extends CrudController<AssetFileService, AssetFile> {

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

    @RequestMapping(value = "/**", method = RequestMethod.GET)
    @NotNull
    public Object getFile(HttpServletRequest request, @RequestParam(value = "dl", required = false) Integer flag_dl) {
        String path = extractFilePath(request);
        boolean is_dl = false;
        if (flag_dl != null) {
            is_dl = flag_dl == 1 ? true : false;
        }
        return service.getFile(path, is_dl);
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