package com.github.schlegel.springjwt.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    private AuthoritiesConstants() {
    }

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String COMPANY_ADMIN = "ROLE_COMPANY_ADMIN";

    public static final String SUPER_ADMIN = "ROLE_SUPER_ADMIN";
}
