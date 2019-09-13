package com.stn.ester.controllers.crud;

import com.stn.ester.etc.base.AccessAllowed;
import com.stn.ester.controllers.base.SecuredController;
import com.stn.ester.entities.SystemProfile;
import com.stn.ester.services.crud.SystemProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.stn.ester.etc.security.SecurityConstants.SYSTEM_PROFILE_URL;

@RestController
public class SystemProfileController extends SecuredController {

    public SystemProfileService service;

    @Autowired
    public SystemProfileController(SystemProfileService systemProfileService) {
        this.service = systemProfileService;
    }

    @PreAuthorize("hasRole(#this.this.superAdminRole())")
    @RequestMapping(value = SYSTEM_PROFILE_URL, method = RequestMethod.PUT)
    public Object update(@Valid @RequestBody SystemProfile systemProfile) {
        return service.updateSingleData(systemProfile);
    }

    @AccessAllowed
    @RequestMapping(value = SYSTEM_PROFILE_URL, method = RequestMethod.GET)
    public Object get() {
        return this.service.get();
    }
}
