package com.stn.ester.rest.controller.api;

import com.stn.ester.rest.dao.jpa.UserRepository;
import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.domain.User;
import com.stn.ester.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController extends AppController<UserService,User>{

    @Autowired
    public UserController(UserService userService){
        super(userService);
    }
}
