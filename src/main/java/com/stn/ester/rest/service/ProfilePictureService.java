package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.ProfilePictureRespository;
import com.stn.ester.rest.domain.ProfilePicture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfilePictureService extends AppService{

    @Autowired
    public ProfilePictureService(ProfilePictureRespository profilePictureRespository){
        super(ProfilePicture.unique_name);
        super.repositories.put(ProfilePicture.unique_name, profilePictureRespository);
    }
}