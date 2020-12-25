package com.lal.userservice.security;

public final class SecurityConstants {

    public static final String SECRET = "mySecretKey";

    public static final long TOKEN_EXPIRATION_TIME = 86400000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String LOGIN_PATH = "/login";
    public static final String REGISTRATION_PATH = "/register";
    public static final String CONFIRMATION_PATH = "/confirm-account";
}
