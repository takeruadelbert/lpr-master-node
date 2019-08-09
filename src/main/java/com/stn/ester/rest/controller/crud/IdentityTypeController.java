package com.stn.ester.rest.controller.crud;

import com.stn.ester.rest.controller.base.CrudController;
import com.stn.ester.rest.domain.IdentityType;
import com.stn.ester.rest.service.IdentityTypeService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/identity_types")
public class IdentityTypeController extends CrudController<IdentityTypeService, IdentityType> {

    @Autowired
    public IdentityTypeController(IdentityTypeService identityTypeServiceService) { super(identityTypeServiceService);}
}