package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.File;
import com.stn.ester.rest.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
public class FileController extends AppController<FileService, File> {

    @Autowired
    public FileController(FileService fileService) { super(fileService); }
}
