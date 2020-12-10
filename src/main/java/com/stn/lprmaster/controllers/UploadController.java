package com.stn.lprmaster.controllers;

import com.stn.ester.core.base.auth.RequireLogin;
import com.stn.ester.core.exceptions.BadRequestException;
import com.stn.lprmaster.services.client.ClientUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private ClientUploadService clientUploadService;

    @Autowired
    public UploadController(ClientUploadService clientUploadService) {
        this.clientUploadService = clientUploadService;
    }

    @RequireLogin
    @PostMapping("/upload")
    public ResponseEntity uploadRaw(@RequestParam MultipartFile file) throws IOException {
        return clientUploadService.uploadRaw(file);
    }

    @RequireLogin
    @PostMapping("/upload-encoded")
    public ResponseEntity uploadEncoded(@RequestBody Map<String, String> data) {
        String filename = data.get("filename");
        if (filename.isEmpty()) {
            throw new BadRequestException("Invalid 'filename'.");
        }
        String encodedFile = data.get("encoded_file");
        if (encodedFile.isEmpty()) {
            throw new BadRequestException("Invalid 'encoded_file'.");
        }
        return clientUploadService.uploadEncoded(filename, encodedFile);
    }

    @RequireLogin
    @PostMapping("/upload-url")
    public ResponseEntity uploadViaUrl(@RequestBody Map<String, List<String>> data) {
        List<String> url = data.get("url");
        return clientUploadService.uploadViaUrl(url);
    }
}