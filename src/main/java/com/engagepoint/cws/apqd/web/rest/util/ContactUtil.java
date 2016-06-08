package com.engagepoint.cws.apqd.web.rest.util;

import com.engagepoint.cws.apqd.domain.Authority;
import com.engagepoint.cws.apqd.domain.User;
import com.engagepoint.cws.apqd.security.AuthoritiesConstants;

import java.util.HashMap;
import java.util.Map;

public final class ContactUtil {
    private static Map<String, String> ROLE_DESCRIPTIONS;

    static {
        ROLE_DESCRIPTIONS = new HashMap<>();
        ROLE_DESCRIPTIONS.put(AuthoritiesConstants.CASE_WORKER, "Case Worker");
        ROLE_DESCRIPTIONS.put(AuthoritiesConstants.PARENT, "Parent");
        ROLE_DESCRIPTIONS.put(AuthoritiesConstants.ADMIN, "Admin");
        ROLE_DESCRIPTIONS.put(AuthoritiesConstants.USER, "User");
    }

    private ContactUtil() {
        // nothing to do
    }

    public static String extractRoleDescription(User user) {
        if (user.getAuthorities() == null || user.getAuthorities().isEmpty()) {
            return "";

        } else {
            Authority authority = user.getAuthorities().iterator().next();

            return ROLE_DESCRIPTIONS.containsKey(authority.getName()) ? ROLE_DESCRIPTIONS.get(authority.getName()) : "";
        }
    }
}
