package com.stn.ester.controllers.crud;

import com.stn.ester.controllers.base.CrudController;
import com.stn.ester.entities.Biodata;
import com.stn.ester.services.crud.BiodataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/biodatas")
public class BiodataController extends CrudController<BiodataService, Biodata> {

    @Autowired
    public BiodataController(BiodataService biodataService) {
        super(biodataService);
    }
}
