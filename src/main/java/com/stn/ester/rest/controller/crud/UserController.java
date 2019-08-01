package com.stn.ester.rest.controller.crud;

import com.stn.ester.rest.controller.base.CrudController;
import com.stn.ester.rest.domain.AppDomain;
import com.stn.ester.rest.domain.LoginSession;
import com.stn.ester.rest.domain.User;
import com.stn.ester.rest.domain.enumerate.Gender;
import com.stn.ester.rest.exception.UnauthorizedException;
import com.stn.ester.rest.helper.SessionHelper;
import com.stn.ester.rest.service.BiodataService;
import com.stn.ester.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController extends CrudController<UserService, User> {

    private UserService userService;
    private BiodataService biodataService;

    @Autowired
    public UserController(UserService userService, BiodataService biodataService) {
        super(userService);
        this.userService = userService;
        this.biodataService = biodataService;
    }

    @Override
    public Object create(@Validated(AppDomain.New.class) @RequestBody User user) {
        return super.service.create(user);
    }

    @Override
    public Object update(@PathVariable long id, @Validated(AppDomain.Existing.class) @RequestBody User user) {
        return super.service.update(id, user);
    }

    @RequestMapping(value = "/heartbeat")
    public User isValid() {
        return SessionHelper.getCurrentUser();
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.PUT)
    public Object changePassword (@RequestBody Map<String, String> data) {
        String old_password = data.get("old_password");
        String new_password = data.get("new_password");
        String retype_new_password = data.get("retype_new_password");
        return service.changePassword(SessionHelper.getUserID(), old_password, new_password, retype_new_password);
    }

    @RequestMapping(value = "/change-profile-picture", method = RequestMethod.POST)
    public Object changeProfilePicture(@RequestBody Map<String, String> data) {
        return service.changeProfilePicture(data.get("token"));
    }

    @RequestMapping(value = "/gender", method = RequestMethod.OPTIONS)
    public Map<Gender, String> getGenderList() {
        return this.biodataService.getGenderList();
    }

}