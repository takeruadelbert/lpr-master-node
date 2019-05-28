package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.Gender;
import com.stn.ester.rest.service.GenderService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/genders")
public class GenderController extends AppController<GenderService, Gender> {

    @Autowired
    public GenderController(GenderService genderService) { super(genderService); }
}