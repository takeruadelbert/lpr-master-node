package com.stn.ester.controllers.crud;

import com.stn.ester.controllers.base.CrudController;
import com.stn.ester.entities.SocialMedia;
import com.stn.ester.services.crud.SocialMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/social_media")
public class SocialMediaController extends CrudController<SocialMediaService, SocialMedia> {

    @Autowired
    public SocialMediaController(SocialMediaService socialMediaService) {
        super(socialMediaService);
    }
}