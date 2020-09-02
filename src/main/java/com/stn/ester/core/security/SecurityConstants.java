package com.stn.ester.core.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static Long EXPIRATION_TIME;
    public static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";
    public static final String AUTHORIZATION_HEADER_STRING = "Authorization";
    public static final String EXPOSE_HEADER_STRING = "Access-Control-Expose-Headers";
    public static final String SIGN_UP_URL = "/users";
    public static final String SYSTEM_PROFILE_URL = "/system_profiles";
    public static final String AUTHORITIES_KEY = "authorities";
    public static final String ROLE_SUPERADMIN = "SUPERADMIN";
    public static final String AUTHORITY_PREFIX = "ACCESS";
    public static final String ROLE_PREFIX = "ROLE";

    @Value("${ester.session.login.timeout}")
    public void setExpirationTime(Long expirationTime) {
        EXPIRATION_TIME = expirationTime;
    }
}
