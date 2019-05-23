package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.Biodata;
import com.stn.ester.rest.service.BiodataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/biodatas")
public class BiodataController extends AppController<BiodataService,Biodata>{

    @Autowired
    public BiodataController(BiodataService biodataService){
        super(biodataService);
    }

}
