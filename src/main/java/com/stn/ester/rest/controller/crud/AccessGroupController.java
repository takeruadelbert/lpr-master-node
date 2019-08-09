package com.stn.ester.rest.controller.crud;

import com.stn.ester.rest.base.DisabledAccess;
import com.stn.ester.rest.base.PageAccess;
import com.stn.ester.rest.controller.base.CrudController;
import com.stn.ester.rest.domain.AccessGroup;
import com.stn.ester.rest.service.AccessGroupService;
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
