package com.engagepoint.cws.apqd.web.filter;

import org.slf4j.MDC;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by dmitry.rudenko on 5/10/2016.
 */
public class CredentialLoggingFilter implements Filter {
    private static final String CREDENTIAL_MESSAGE = "CREDENTIAL_MESSAGE";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // nothing to do
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String userName = getUserName();
        String sessionId = getSessionId();
        MDC.put(CREDENTIAL_MESSAGE, "username: " + userName + ", sessionId: " + sessionId);
        filterChain.doFilter(servletRequest, servletResponse);
    }


    private String getUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "anonymous";
        }
        return authentication.getName();
    }

    private String getSessionId() {
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }

    @Override
    public void destroy() {
        // nothing to do
    }
}
