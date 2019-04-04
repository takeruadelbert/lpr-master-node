package com.stn.ester.rest.controller.api;

import com.stn.ester.rest.domain.Biodata;
import com.stn.ester.rest.domain.User;
import com.stn.ester.rest.service.BiodataService;
import com.stn.ester.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/biodata")
public class BiodataController extends AppController<BiodataService,Biodata>{

    @Autowired
    public BiodataController(BiodataService biodataService){
        super(biodataService);
    }

}
