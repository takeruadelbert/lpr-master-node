package com.stn.ester.controllers.crud;

import com.stn.ester.controllers.base.CrudController;
import com.stn.ester.entities.Module;
import com.stn.ester.services.crud.ModuleService;
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
