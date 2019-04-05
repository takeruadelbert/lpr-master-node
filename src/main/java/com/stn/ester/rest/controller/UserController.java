package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.User;
import com.stn.ester.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController extends AppController<UserService,User>{

    @Autowired
    public UserController(UserService userService){
        super(userService);
    }
}
