package com.stn.ester.rest.controller.crud;

import com.stn.ester.rest.base.DisabledAccess;
import com.stn.ester.rest.base.PageAccess;
import com.stn.ester.rest.controller.base.CrudController;
import com.stn.ester.rest.domain.ModuleLink;
import com.stn.ester.rest.service.ModuleLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@DisabledAccess({PageAccess.CREATE,PageAccess.GET,PageAccess.INDEX,PageAccess.LIST,PageAccess.UPDATE})
@RequestMapping("/module_links")
public class ModuleLinkController extends CrudController<ModuleLinkService, ModuleLink> {

    @Autowired
    public ModuleLinkController(ModuleLinkService moduleLinkService){
        super(moduleLinkService);
    }
}
