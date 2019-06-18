package com.stn.ester.rest.controller;

import com.stn.ester.rest.dao.jpa.ProfilePictureRespository;
import com.stn.ester.rest.domain.File;
import com.stn.ester.rest.domain.ProfilePicture;
import com.stn.ester.rest.service.ProfilePictureService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.FileTypeMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.beans.XMLDecoder;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

@RestController
@RequestMapping("/profile_pictures")
public class ProfilePictureController extends AppController<ProfilePictureService, ProfilePicture>{

    @Autowired
    private ProfilePictureService profilePictureService;

    @Autowired
    private ProfilePictureRespository profilePictureRespository;

    @Autowired
    public ProfilePictureController(ProfilePictureService profilePictureService){
        super(profilePictureService);
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object convertImage(@RequestParam("file") MultipartFile file) throws IOException {

        //serv image to resource folder
        String path2 = System.getProperty("user.dir") + "\\src\\main\\resources\\files\\images\\" + file.getOriginalFilename(); //set path directory

        FileOutputStream fout = new FileOutputStream(path2);
        fout.write(file.getBytes());
        fout.close();
        //end

        String imagePath = System.getProperty("user.dir") + "\\src\\main\\resources\\files\\images\\" + file.getOriginalFilename();
        String base64Image = "";

        try (FileInputStream imageInFile = new FileInputStream(imagePath)){
            System.out.println("imageInFile = " + imageInFile);
            // Reading a Image file from file system
            byte[] imageData;
            int maxByteImage = 1000000;

            imageData = new byte[maxByteImage];
            imageInFile.read(imageData);
            base64Image = Base64.getEncoder().encodeToString(imageData);

            ProfilePicture profilePicture = new ProfilePicture();
            profilePicture.avatar = base64Image;

            profilePictureService.save(profilePicture);

        } catch (FileNotFoundException e) {
            System.out.println("Image not found" + e);
            e.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("Exception while reading the image" + ioe);
            ioe.printStackTrace();
        }
        return base64Image;
    }
}