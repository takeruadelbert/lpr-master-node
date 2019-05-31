package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.UserGroup;
import com.stn.ester.rest.service.AccessGroupService;
import com.stn.ester.rest.service.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/user_groups")
public class UserGroupController extends AppController<UserGroupService, UserGroup> {

    AccessGroupService accessGroupService;

    @Autowired
    public UserGroupController(UserGroupService usergroupService, AccessGroupService accessGroupService) {
        super(usergroupService);
        this.accessGroupService=accessGroupService;
    }

    @RequestMapping(value ="/{id}/access_groups", method = RequestMethod.GET)
    public Object getAccessGroup(@PathVariable long id){
        return service.getAccessGroup(id);
    }

    @RequestMapping(value ="/{id}/access_groups", method = RequestMethod.PUT)
    public Object editAccessGroup(@PathVariable long id,@RequestBody HashMap<String,Object> o){
        System.out.println(o);
        ArrayList<HashMap> accessGroups= (ArrayList) o.get("accessGroups");
        Long userGroupId=Long.parseLong(o.get("id").toString());
        this.accessGroupService.updateAll(userGroupId,accessGroups);
        return service.getAccessGroup(id);
    }
}
