package com.stn.ester.controllers.crud;

import com.stn.ester.controllers.base.CrudController;
import com.stn.ester.entities.Role;
import com.stn.ester.services.crud.AccessGroupService;
import com.stn.ester.services.crud.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/user_groups")
public class RoleController extends CrudController<RoleService, Role> {

    AccessGroupService accessGroupService;

    @Autowired
    public RoleController(RoleService usergroupService, AccessGroupService accessGroupService) {
        super(usergroupService);
        this.accessGroupService = accessGroupService;
    }

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole('viewAccessGroup'))")
    @RequestMapping(value = "/{id}/access_groups", method = RequestMethod.GET)
    public Object getAccessGroup(@PathVariable long id) {
        return service.getAccessGroup(id);
    }

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole('viewAccessGroup'))")
    @RequestMapping(value = "/{id}/access_groups/v2", method = RequestMethod.GET)
    public Object getAccessGroupV2(@PathVariable long id) {
        return service.getRoleAccessGroup(id);
    }

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole('editAccessGroup'))")
    @RequestMapping(value = "/{id}/access_groups", method = RequestMethod.PUT)
    public Object editAccessGroup(@PathVariable long id, @RequestBody HashMap<String, Object> o) {
        System.out.println(o);
        ArrayList<HashMap> accessGroups = (ArrayList) o.get("accessGroups");
        Long roleId = Long.parseLong(o.get("id").toString());
        this.accessGroupService.updateAll(roleId, accessGroups);
        return service.getAccessGroup(id);
    }

    @Override
    @Transactional
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Object create(@Valid @RequestBody Role role) {
        return service.create(role);
    }


}
