package com.stn.ester.services.crud;

import com.stn.ester.entities.SocialMedia;
import com.stn.ester.repositories.jpa.SocialMediaRepository;
import com.stn.ester.services.base.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocialMediaService extends CrudService {

    @Autowired
    public SocialMediaService(SocialMediaRepository socialMediaRepository) {
        super(SocialMedia.class, socialMediaRepository);
        super.repositories.put(SocialMedia.class.getName(), socialMediaRepository);
    }
}