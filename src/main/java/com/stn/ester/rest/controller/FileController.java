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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/files")
public class FileController extends AppController<FileService, File> {

    @Autowired
    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) { super(fileService);}

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        String path = System.getProperty("user.dir") + "\\src\\main\\resources\\files\\" + file.getOriginalFilename(); //set path directory
        if (file.getSize() < 1000000) {
            try(FileOutputStream fout = new FileOutputStream(path)) {
                fout.write(file.getBytes());
                fout.close();

                System.out.println("File is successfully uploaded.");
            } catch (FileNotFoundException e) {
                System.out.println("File not found" + e);
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("Maximum upload size exceeded 10MB.");
        }
        return "File is successfully uploaded.";
    }

    @RequestMapping(value = "/image/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object uploadPhoto(@RequestParam("file") MultipartFile file) throws IOException {

        if (file.getContentType().equals("image/png") || file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/gif")) {
            //serv image to resource folder
            String path = System.getProperty("user.dir") + "\\src\\main\\resources\\files\\images\\" + file.getOriginalFilename(); //set path directory
            FileOutputStream fout = new FileOutputStream(path);
            fout.write(file.getBytes());
            fout.close();
            //end

            String imagePath = System.getProperty("user.dir") + "\\src\\main\\resources\\files\\images\\" + file.getOriginalFilename();
            String base64Image = "";

            try (FileInputStream imageInFile = new FileInputStream(imagePath)) {
                // Reading a Image file from file system
                byte[] imageData;
                int maxByteImage = 1000000;

                imageData = new byte[maxByteImage];
                imageInFile.read(imageData);
                base64Image = Base64.getEncoder().encodeToString(imageData);

                File files = new File();
                files.name = base64Image;
                fileService.save(files);

                System.out.println("Image is successfully uploaded.");
            } catch (FileNotFoundException e) {
                System.out.println("Image not found" + e);
                e.printStackTrace();
            }
            return base64Image;
        } else {
            throw new IllegalArgumentException("Image not uploaded.");
        }
    }
}
