package com.stn.ester.rest.controller;

import com.stn.ester.rest.dao.jpa.ProfilePictureRespository;
import com.stn.ester.rest.domain.File;
import com.stn.ester.rest.domain.ProfilePicture;
import com.stn.ester.rest.service.ProfilePictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.Base64;

@RestController
@RequestMapping("/profile_pictures")
public class ProfilePictureController extends AppController<ProfilePictureService, ProfilePicture>{

    @Autowired
    public ProfilePictureController(ProfilePictureService profilePictureService){
        super(profilePictureService);
    }
}