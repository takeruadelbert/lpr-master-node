package com.stn.ester.rest.service;

import com.stn.ester.rest.dao.jpa.SocialMediaRepository;
import com.stn.ester.rest.domain.SocialMedia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocialMediaService extends AppService {

    @Autowired
    public SocialMediaService(SocialMediaRepository socialMediaRepository) {
        super(SocialMedia.unique_name);
        super.repositories.put(SocialMedia.unique_name, socialMediaRepository);
    }
}