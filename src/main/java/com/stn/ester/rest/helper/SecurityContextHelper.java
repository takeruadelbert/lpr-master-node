package com.stn.ester.rest.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public class SecurityContextHelper {

    public static Authentication getAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
