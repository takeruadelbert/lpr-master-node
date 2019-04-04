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
public class BiodataController {

    private BiodataService biodataService;

    @Autowired
    public BiodataController(BiodataService biodataService){
        this.biodataService=biodataService;
    }

    @RequestMapping(value ="", method = RequestMethod.POST)
    public Object create(@RequestBody Biodata domain){
        return biodataService.create(domain);
    }

    @RequestMapping(value ="/{id}", method = RequestMethod.PUT)
    public Object update(@PathVariable long id,@RequestBody Biodata domain){
        return biodataService.update(id,domain);
    }

}
