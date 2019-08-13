package com.stn.ester.rest.controller.crud;

import com.stn.ester.rest.controller.base.CrudController;
import com.stn.ester.rest.domain.SocialMedia;
import com.stn.ester.rest.service.SocialMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/social_media")
public class SocialMediaController extends CrudController<SocialMediaService, SocialMedia> {

    @Autowired
    public SocialMediaController(SocialMediaService socialMediaService) { super(socialMediaService);}
}