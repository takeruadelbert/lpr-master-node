package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.ProfilePicture;
import com.stn.ester.rest.service.ProfilePictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile_pictures")
public class ProfilePictureController extends AppController<ProfilePictureService, ProfilePicture>{

    @Autowired
    public ProfilePictureController(ProfilePictureService profilePictureService){
        super(profilePictureService);
    }
}