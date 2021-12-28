package com.mauvaisetroupe.eadesignit.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String CONTRIBUTOR = "ROLE_CONTRIBUTOR";

    public static final String WRITE = "ROLE_WRITE";

    public static final String HARD_DELETE = "ROLE_HARD_DELETE";

    private AuthoritiesConstants() {}
}
