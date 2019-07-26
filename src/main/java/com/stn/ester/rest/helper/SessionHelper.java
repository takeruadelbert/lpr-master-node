package com.stn.ester.rest.helper;

import com.stn.ester.rest.domain.User;
import com.stn.ester.rest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.stn.ester.rest.security.SecurityConstants.ROLE_PREFIX;
import static com.stn.ester.rest.security.SecurityConstants.ROLE_SUPERADMIN;

@Component
public class SessionHelper {

    public static UserService userService;

    @Autowired
    public SessionHelper(UserService userService) {
        SessionHelper.userService = userService;
    }

    public static Long getUserID() {
        return getCurrentUser().getId();
    }

    public static Long getDepartmentID() {
        return null;
    }

    public static User getCurrentUser() {
        //jwt set username on auth, not user object
        Authentication auth = SecurityContextHelper.getAuthentication();
        String username = auth.getPrincipal().toString();
        return (User) userService.loadUserByUsername(username);
    }

    public static boolean isSuperAdmin() {
        return SecurityContextHelper.getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority(ROLE_PREFIX + "_" + ROLE_SUPERADMIN));
    }
}
