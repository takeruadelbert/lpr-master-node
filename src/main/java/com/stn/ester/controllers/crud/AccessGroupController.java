package com.stn.ester.controllers.crud;

import com.stn.ester.core.base.DisabledAccess;
import com.stn.ester.core.base.PageAccess;
import com.stn.ester.controllers.base.CrudController;
import com.stn.ester.entities.AccessGroup;
import com.stn.ester.services.crud.AccessGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/access_groups")
@DisabledAccess({PageAccess.CREATE,PageAccess.UPDATE,PageAccess.DELETE})
public class AccessGroupController extends CrudController<AccessGroupService, AccessGroup> {

    @Autowired
    public AccessGroupController(AccessGroupService accessGroupService) {
        super(accessGroupService);
    }

}
