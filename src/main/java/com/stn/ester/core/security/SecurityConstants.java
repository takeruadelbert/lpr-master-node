package com.stn.ester.core.security;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String EXPOSE_HEADER_STRING = "Access-Control-Expose-Headers";
    public static final String SIGN_UP_URL = "/users";
    public static final String SYSTEM_PROFILE_URL = "/system_profiles";
    public static final String AUTHORITIES_KEY = "authorities";
    public static final String ROLE_SUPERADMIN = "SUPERADMIN";
    public static final String AUTHORITY_PREFIX = "ACCESS";
    public static final String ROLE_PREFIX = "ROLE";
}
