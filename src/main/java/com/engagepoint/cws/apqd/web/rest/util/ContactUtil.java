package com.engagepoint.cws.apqd.web.rest.util;

import com.engagepoint.cws.apqd.domain.Authority;
import com.engagepoint.cws.apqd.domain.User;
import com.engagepoint.cws.apqd.security.AuthoritiesConstants;

public final class ContactUtil {
    private ContactUtil() {
        // nothing to do
    }

    public static String extractRoleDescription(User user) {
        if (user.getAuthorities() == null || user.getAuthorities().isEmpty()) {
            return "";
        }

        Authority authority = user.getAuthorities().iterator().next();

        if (authority.getName().equals(AuthoritiesConstants.CASE_WORKER)) {
            return "Case Worker";
        } else if (authority.getName().equals(AuthoritiesConstants.PARENT)) {
            return "Parent";
        } else if (authority.getName().equals(AuthoritiesConstants.ADMIN)) {
            return "Admin";
        } else if (authority.getName().equals(AuthoritiesConstants.USER)) {
            return "User";
        } else {
            return "";
        }
    }
}
