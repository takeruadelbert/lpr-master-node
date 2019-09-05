package com.stn.ester.rest.controller.crud;

import com.stn.ester.rest.base.AccessAllowed;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController extends CrudController<UserService, User> {
    private static final String REQUEST_MAPPING_IDENTIFY_EMAIL =  "/identify_email";
    private static final String REQUEST_MAPPING_PASSWORD_RESET = "/password_reset/{token}";
    private static final String PAYLOAD_EMAIL = "email";
    private static final String PAYLOAD_NEW_PASSWORD = "new_password";
    private static final String PAYLOAD_CONFIRM_PASSWORD = "confirm_password";
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

    @PreAuthorize("hasPermission(null,'allowall')")
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

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole('changeUserPassword'))")
    @RequestMapping(value = "/change-password/{id}", method = RequestMethod.PUT)
    public Object changePassword (@RequestBody Map<String, String> data,@PathVariable long id) {
        String new_password = data.get("new_password");
        String retype_new_password = data.get("retype_new_password");
        return service.changePassword(id, new_password, retype_new_password);
    }

    @PreAuthorize("hasPermission(null,'allowall')")
    @RequestMapping(value = "/change-profile-picture", method = RequestMethod.POST)
    public Object changeProfilePicture(@RequestBody Map<String, String> data) {
        return service.changeProfilePicture(data.get("token"));
    }

    @PreAuthorize("hasPermission(null,'allowall')")
    @RequestMapping(value = "/gender", method = RequestMethod.OPTIONS)
    public Map<Gender, String> getGenderList() {
        return this.biodataService.getGenderList();
    }

    @AccessAllowed
    @RequestMapping(value = REQUEST_MAPPING_IDENTIFY_EMAIL, method = RequestMethod.POST)
    public Object identifyEmail(@Valid @RequestBody Map<String, String> payload, HttpServletRequest request) {
        return userService.identifyEmail(payload.get(PAYLOAD_EMAIL), request);
    }

    @AccessAllowed
    @RequestMapping(value = REQUEST_MAPPING_PASSWORD_RESET, method = RequestMethod.PUT)
    public Object passwordReset(@PathVariable String token, @Valid @RequestBody(required = false) Map<String, String> data) {
        String new_password = data.get(PAYLOAD_NEW_PASSWORD);
        String confirm_password = data.get(PAYLOAD_CONFIRM_PASSWORD);
        return userService.passwordReset(token, new_password, confirm_password);
    }

}