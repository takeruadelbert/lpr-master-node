package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.User;
import com.stn.ester.rest.domain.UserGroup;
import com.stn.ester.rest.dto.UserDto;
import com.stn.ester.rest.service.UserGroupService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/user_groups")
public class UserGroupController extends AppController<UserGroupService, UserGroup> {



    @Autowired
    public UserGroupController(UserGroupService usergroupService) {
        super(usergroupService);
    }

}
