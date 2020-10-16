package com.stn.ester.controllers.crud;

import com.stn.ester.controllers.base.SecuredController;
import com.stn.ester.core.base.auth.AccessAllowed;
import com.stn.ester.core.base.auth.RequireSuperAdmin;
import com.stn.ester.entities.SystemProfile;
import com.stn.ester.services.crud.SystemProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.stn.ester.core.security.SecurityConstants.SYSTEM_PROFILE_URL;

@RestController
public class SystemProfileController extends SecuredController {

    public SystemProfileService service;

    @Autowired
    public SystemProfileController(SystemProfileService systemProfileService) {
        this.service = systemProfileService;
    }

    @RequireSuperAdmin
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
