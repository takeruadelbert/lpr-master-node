package com.stn.ester.helpers;

import com.stn.ester.entities.User;
import com.stn.ester.services.crud.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import static com.stn.ester.core.security.SecurityConstants.ROLE_PREFIX;
import static com.stn.ester.core.security.SecurityConstants.ROLE_SUPERADMIN;

@Component
public class SessionHelper {

    public static UserService userService;

    @Autowired
    public SessionHelper(UserService userService) {
        SessionHelper.userService = userService;
    }

    public static Long getUserID() {
        User user=getCurrentUser();
        if (user!=null) {
            return user.getId();
        }else{
            return null;
        }
    }

    public static Long getUserGroupId(){
        User user=getCurrentUser();
        if (user!=null) {
            return user.getUserGroup().getId();
        }else{
            return null;
        }
    }

    public static Long getDepartmentID() {
        return null;
    }

    public static User getCurrentUser() {
        //jwt set username on auth, not user object
        Authentication auth = SecurityContextHelper.getAuthentication();
        if (auth!=null) {
            String username = auth.getPrincipal().toString();
            return (User) userService.loadUserByUsername(username);
        }else{
            return null;
        }
    }

    public static boolean isSuperAdmin() {
        return SecurityContextHelper.getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority(ROLE_PREFIX + "_" + ROLE_SUPERADMIN));
    }
}
