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
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }

    @RequestMapping(value="", method = RequestMethod.GET)
    public Page<Object> index(@RequestParam(name="page") Integer page, @RequestParam(name="size") Integer size){
        return userService.index(page,size);
    }

    @RequestMapping(value ="", method = RequestMethod.POST)
    public Object create(@RequestBody User domain){
        return userService.create(domain);
    }

    @RequestMapping(value ="/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable long id){
        return userService.get(id);
    }

    @RequestMapping(value ="/{id}", method = RequestMethod.PUT)
    public Object update(@PathVariable long id,@RequestBody User domain){
        return userService.update(id,domain);
    }

}
