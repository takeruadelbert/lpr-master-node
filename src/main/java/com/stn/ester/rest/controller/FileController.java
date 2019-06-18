package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.File;
import com.stn.ester.rest.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController extends AppController<FileService, File> {

    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) { super(fileService);}

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        final long limit = 5000000;

        String path = System.getProperty("user.dir") + "\\src\\main\\resources\\files\\" + file.getOriginalFilename(); //set path directory
        if (file.getSize() > limit){

            try(FileOutputStream fout = new FileOutputStream(path)) {
                fout.write(file.getBytes());
                fout.close();
            }catch (FileNotFoundException e) {
                System.out.println("File not found" + e);
                e.printStackTrace();
            }
            throw new MaxUploadSizeExceededException(limit);
        }
        return file;
    }
}
