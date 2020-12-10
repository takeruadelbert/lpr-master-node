package com.stn.lprmaster.controllers;

import com.stn.ester.core.base.auth.RequireLogin;
import com.stn.lprmaster.services.client.ClientUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
}