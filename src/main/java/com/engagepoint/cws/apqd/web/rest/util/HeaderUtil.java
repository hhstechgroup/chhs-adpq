package com.engagepoint.cws.apqd.web.rest.util;

import org.springframework.http.HttpHeaders;

/**
 * Utility class for http header creation.
 *
 */
public final class HeaderUtil {
    private HeaderUtil() {
    }

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-apqdApp-alert", message);
        headers.add("X-apqdApp-params", param);
        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(String entityName, String param) {
        return createAlert("apqdApp." + entityName + ".created", param);
    }
    public static HttpHeaders createEntityUpdateAlert(String entityName, String param) {
        return createAlert("apqdApp." + entityName + ".updated", param);
    }

    public static HttpHeaders createEntityDeletionAlert(String entityName, String param) {
        return createAlert("apqdApp." + entityName + ".deleted", param);
    }

    @SuppressWarnings("squid:S1172") // all auto-generated Resource classes uses such signature and passes the defaultMessage
    public static HttpHeaders createFailureAlert(String entityName, String errorKey, String defaultMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-apqdApp-error", "error." + errorKey);
        headers.add("X-apqdApp-params", entityName);
        return headers;
    }
}
