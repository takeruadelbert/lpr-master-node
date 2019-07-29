package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.SystemProfile;
import com.stn.ester.rest.service.SystemProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class SystemProfileController {

    public SystemProfileService service;

    @Autowired
    public SystemProfileController(SystemProfileService systemProfileService) {
        this.service=systemProfileService;
    }

    @RequestMapping(value = "/system_profiles", method = RequestMethod.PUT)
    public Object update(@Valid @RequestBody SystemProfile systemProfile) {
        return service.updateSingleData(systemProfile);
    }

    @RequestMapping(value = "/system_profiles", method = RequestMethod.GET)
    public Object get() {
        return this.service.get();
    }
}
