package com.stn.ester.rest.controller.crud;

import com.stn.ester.rest.base.AccessAllowed;
import com.stn.ester.rest.controller.base.SecuredController;
import com.stn.ester.rest.domain.SystemProfile;
import com.stn.ester.rest.service.SystemProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.stn.ester.rest.security.SecurityConstants.SYSTEM_PROFILE_URL;

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
