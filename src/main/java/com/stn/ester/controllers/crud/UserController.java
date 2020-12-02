package com.stn.ester.controllers.crud;

import com.stn.ester.controllers.base.CrudController;
import com.stn.ester.core.base.auth.AccessAllowed;
import com.stn.ester.core.base.auth.RequireLogin;
import com.stn.ester.core.events.HeartbeatEvent;
import com.stn.ester.dto.UserProfileDTO;
import com.stn.ester.entities.User;
import com.stn.ester.entities.enumerate.Gender;
import com.stn.ester.entities.enumerate.UserStatus;
import com.stn.ester.entities.validationgroups.Create;
import com.stn.ester.entities.validationgroups.Update;
import com.stn.ester.helpers.SessionHelper;
import com.stn.ester.services.crud.BiodataService;
import com.stn.ester.services.crud.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController extends CrudController<UserService, User> {
    private static final String REQUEST_MAPPING_IDENTIFY_EMAIL = "/identify_email";
    private static final String REQUEST_MAPPING_PASSWORD_RESET = "/password_reset/{token}";
    private static final String PAYLOAD_EMAIL = "email";
    private static final String PAYLOAD_NEW_PASSWORD = "new_password";
    private static final String PAYLOAD_CONFIRM_PASSWORD = "confirm_password";
    private UserService userService;
    private BiodataService biodataService;
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public UserController(UserService userService, BiodataService biodataService, ApplicationEventPublisher applicationEventPublisher) {
        super(userService);
        this.userService = userService;
        this.biodataService = biodataService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Object create(@Validated(Create.class) @RequestBody User user) {
        return super.service.create(user);
    }

    @Override
    public Object update(@PathVariable long id, @Validated(Update.class) @RequestBody User user, @RequestBody Map<String, Object> requestBody) {
        return super.service.update(id, user, requestBody);
    }

    @RequireLogin
    @RequestMapping(value = "/heartbeat")
    public void isValid(HttpServletRequest request) {
        applicationEventPublisher.publishEvent(new HeartbeatEvent(this, SessionHelper.getUserID(), request));
    }

    @RequireLogin
    @RequestMapping(value = "/profile")
    public UserProfileDTO getMyProfile() {
        return new UserProfileDTO(SessionHelper.getCurrentUser());
    }

    @RequireLogin
    @RequestMapping(value = "/change-password", method = RequestMethod.PUT)
    public Object changePassword(@RequestBody Map<String, String> data) {
        String old_password = data.get("old_password");
        String new_password = data.get("new_password");
        String retype_new_password = data.get("retype_new_password");
        return service.changePassword(SessionHelper.getUserID(), old_password, new_password, retype_new_password);
    }

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole('changeUserPassword'))")
    @RequestMapping(value = "/change-password/{id}", method = RequestMethod.PUT)
    public Object changePassword(@RequestBody Map<String, String> data, @PathVariable Long id) {
        String new_password = data.get("new_password");
        String retype_new_password = data.get("retype_new_password");
        return service.changePassword(id, new_password, retype_new_password);
    }

    @RequireLogin
    @RequestMapping(value = "/change-profile-picture", method = RequestMethod.POST)
    public Object changeProfilePicture(@RequestBody Map<String, String> data) {
        return service.changeProfilePicture(data.get("token"));
    }

    @RequireLogin
    @RequestMapping(value = "/gender", method = RequestMethod.OPTIONS)
    public Map<Gender, String> getGenderList() {
        return this.biodataService.getGenderList();
    }

    @RequireLogin
    @RequestMapping(value = "/status", method = RequestMethod.OPTIONS)
    public Map<UserStatus, String> getUserStatusList() {
        return this.userService.getUserStatusList();
    }

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole('changeUserStatus'))")
    @RequestMapping(value = "/changeStatus", method = RequestMethod.PUT)
    public void changeUserStatus(@RequestBody Map<String, String> payload) {
        this.userService.changeUserStatus(Long.parseLong(payload.get("id")), UserStatus.getIfPresentOrThrowError(payload.get("userStatus")));
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

    @PreAuthorize("hasRole(#this.this.readCurrentUserRole())")
    @RequestMapping(value = "search/superadmin", method = RequestMethod.OPTIONS)
    public Collection searchSuperAdmin(@RequestParam(name = "keyword") String keyword) {
        return service.searchSuperAdmin(keyword);
    }

}