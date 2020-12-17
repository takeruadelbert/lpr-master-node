package com.stn.lprmaster.controllers;

import com.stn.ester.core.base.auth.RequireLogin;
import com.stn.ester.core.exceptions.BadRequestException;
import com.stn.lprmaster.client.webclient.LprClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class UploadController {
    private LprClient lprClient;

    @Autowired
    public UploadController(LprClient lprClient) {
        this.lprClient = lprClient;
    }

    @RequireLogin
    @PostMapping("/upload")
    public Object uploadRaw(@RequestParam MultipartFile file) throws IOException {
        return lprClient.uploadRaw(file);
    }

    @RequireLogin
    @PostMapping("/upload-encoded")
    public Object uploadEncoded(@RequestBody Map<String, String> data) {
        String filename = data.get("filename");
        if (filename.isEmpty()) {
            throw new BadRequestException("Invalid 'filename'.");
        }
        String encodedFile = data.get("encoded_file");
        if (encodedFile.isEmpty()) {
            throw new BadRequestException("Invalid 'encoded_file'.");
        }
        return lprClient.uploadEncoded(filename, encodedFile);
    }

    @RequireLogin
    @PostMapping("/upload-url")
    public Object uploadViaUrl(@RequestBody Map<String, List<String>> data) {
        List<String> url = data.get("url");
        return lprClient.uploadViaUrl(url);
    }
}