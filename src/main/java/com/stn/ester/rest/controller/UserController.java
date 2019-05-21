package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.LoginSession;
import com.stn.ester.rest.domain.User;
// import com.stn.ester.rest.dto.UserDto;
import com.stn.ester.rest.exception.SessionExpireException;
import com.stn.ester.rest.exception.UnauthorizedException;
import com.stn.ester.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController extends AppController<UserService, User> {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        super(userService);
        this.userService=userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map login(@RequestBody Map<String, String> payload, HttpSession session) {
        Map<String, Object> loginToken = service.login(payload.get("username"), payload.get("password"), session);
        return loginToken;
    }

    @RequestMapping(value = "/heartbeat")
    public Object isValid(@RequestHeader("access-token") String accessToken) {
        LoginSession loginSession=this.userService.tokenHeartbeat(accessToken);
        if (loginSession==null){
            throw new UnauthorizedException();
        }else{
            return loginSession;
        }
    }
}
