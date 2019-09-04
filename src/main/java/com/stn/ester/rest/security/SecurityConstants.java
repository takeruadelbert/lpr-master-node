package com.stn.ester.rest.security;

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
    public static final String VALIDATOR_URL_MESSAGE = "Invalid URL.";
    public static final String NOT_BLANK_URL_MESSAGE = "Url is mandatory.";
    public static final String SYSTEM_PROFILE_ID_COLUMN = "system_profile_id";
}
