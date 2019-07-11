package com.stn.ester.rest.controller;

import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.domain.LoginSession;
import com.stn.ester.rest.domain.User;
import com.stn.ester.rest.exception.UnauthorizedException;
import com.stn.ester.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController extends AppController<UserService, User> {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }

    @Override
    public Object create(@Validated(AppDomain.New.class) @RequestBody User user) {
        return super.service.create(user);
    }

    @Override
    public Object update(@PathVariable long id, @Validated(AppDomain.Existing.class) @RequestBody User user) {
        return super.service.update(id, user);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map login(@RequestBody Map<String, String> payload, HttpSession session) {
        Map<String, Object> loginToken = service.login(payload.get("username"), payload.get("password"), session);
        return loginToken;
    }

    @RequestMapping(value = "/heartbeat")
    public Object isValid(@RequestHeader("access-token") String accessToken) {
        LoginSession loginSession = this.userService.tokenHeartbeat(accessToken);
        if (loginSession == null) {
            throw new UnauthorizedException();
        } else {
            return loginSession;
        }
    }

    @RequestMapping(value = "/{id}/change-password", method = RequestMethod.PUT)
    public Object changePassword(@PathVariable Long id, @RequestBody Map<String, String> data) {
        String old_password = data.get("old_password");
        String new_password = data.get("new_password");
        String retype_new_password = data.get("retype_new_password");
        return service.changePassword(id, old_password, new_password, retype_new_password);
    }

    @RequestMapping(value = "/change-profile-picture", method = RequestMethod.POST)
    public Object changeProfilePicture(@RequestBody Map<String, String> data) {
        return service.changeProfilePicture(data.get("token"));
    }

    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public Object resetPassword(@RequestBody Map<String, String> payload) {
        return service.resetPassword(payload.get("email"));
    }
}