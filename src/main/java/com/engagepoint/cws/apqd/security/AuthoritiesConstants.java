package com.engagepoint.cws.apqd.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String INTAKE_WORKER = "ROLE_INTAKE_WORKER";

    public static final String INVESTIGATOR = "ROLE_INVESTIGATOR";

    private AuthoritiesConstants() {
    }
}
