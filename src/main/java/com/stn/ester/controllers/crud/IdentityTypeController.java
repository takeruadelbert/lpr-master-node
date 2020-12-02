package com.stn.ester.controllers.crud;

import com.stn.ester.controllers.base.CrudController;
import com.stn.ester.entities.IdentityType;
import com.stn.ester.services.crud.IdentityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/identity_types")
public class IdentityTypeController extends CrudController<IdentityTypeService, IdentityType> {

    @Autowired
    public IdentityTypeController(IdentityTypeService identityTypeServiceService) {
        super(identityTypeServiceService);
    }
}