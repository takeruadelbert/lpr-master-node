package com.stn.ester.rest.controller.crud;

import com.stn.ester.rest.controller.base.CrudController;
import com.stn.ester.rest.domain.Module;
import com.stn.ester.rest.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/modules")
public class ModuleController extends CrudController<ModuleService, Module> {

    @Autowired
    public ModuleController(ModuleService moduleService){
        super(moduleService);
    }

}
