package com.stn.ester.controllers.crud;

import com.stn.ester.controllers.base.CrudController;
import com.stn.ester.core.base.DisabledAccess;
import com.stn.ester.core.base.PageAccess;
import com.stn.ester.entities.ModuleLink;
import com.stn.ester.services.crud.ModuleLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@DisabledAccess({PageAccess.CREATE, PageAccess.GET, PageAccess.INDEX, PageAccess.LIST, PageAccess.UPDATE})
@RequestMapping("/module_links")
public class ModuleLinkController extends CrudController<ModuleLinkService, ModuleLink> {

    @Autowired
    public ModuleLinkController(ModuleLinkService moduleLinkService) {
        super(moduleLinkService);
    }
}
