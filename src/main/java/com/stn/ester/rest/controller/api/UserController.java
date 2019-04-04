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
public class UserController extends AppController<UserService>{

    @Autowired
    public UserController(UserService userService){
        super(userService);
    }

    @RequestMapping(value ="", method = RequestMethod.POST)
    public Object create(@RequestBody User domain){
        return service.create(domain);
    }

    @RequestMapping(value ="/{id}", method = RequestMethod.PUT)
    public Object update(@PathVariable long id,@RequestBody User domain){
        return service.update(id,domain);
    }

}
