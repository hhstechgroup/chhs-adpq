package com.engagepoint.cws.apqd.web.rest.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by alexander.serbin on 6/6/2016.
 */
public final class HttpRequestUtil {

    private HttpRequestUtil() {}

    public static String buildBaseUrl(HttpServletRequest request) {
        return  request.getScheme() + // "http"
            "://" +                                // "://"
            request.getServerName() +              // "myhost"
            ":" +                                  // ":"
            request.getServerPort() +              // "80"
            request.getContextPath();              // "/myContextPath" or "" if deployed in root context
    }

}
