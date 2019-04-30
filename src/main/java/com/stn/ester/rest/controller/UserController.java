package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.User;
import com.stn.ester.rest.dto.UserDto;
import com.stn.ester.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController extends AppController<UserService,User>{

    @Autowired
    public UserController(UserService userService){
        super(userService);
    }

    @RequestMapping(value ="/login", method = RequestMethod.POST)
    public Map login(@RequestBody Map<String,String> payload){
        return service.login(payload.get("username"),payload.get("password"));
    }

}
