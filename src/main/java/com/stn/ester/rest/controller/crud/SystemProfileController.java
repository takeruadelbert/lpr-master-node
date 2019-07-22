package com.stn.ester.rest.controller.crud;

import com.stn.ester.rest.controller.base.CrudController;
import com.stn.ester.rest.domain.SystemProfile;
import com.stn.ester.rest.service.SystemProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class SystemProfileController extends CrudController<SystemProfileService, SystemProfile> {
    @Autowired
    public SystemProfileController(SystemProfileService systemProfileService) {
        super(systemProfileService);
    }

    @Override
    @RequestMapping(value = "/system_profiles", method = RequestMethod.PUT)
    public Object update(@Valid @RequestBody SystemProfile systemProfile) {
        return service.updateSingleData(systemProfile);
    }
}
