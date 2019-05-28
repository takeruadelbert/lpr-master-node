package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.TypeIdentity;
import com.stn.ester.rest.service.TypeIdentityService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/type_indentities")
public class TypeIdentityController extends AppController<TypeIdentityService, TypeIdentity> {

    @Autowired
    public TypeIdentityController(TypeIdentityService typeService) { super(typeService);}
}