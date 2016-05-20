package com.engagepoint.cws.apqd.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by dmytro.palczewski on 3/31/2016.
 */
@ConfigurationProperties(prefix = "usps", ignoreUnknownFields = false)
public class UspsProperties {

    private  String serverUrl;

    private  String userId;

    private  int timeout;

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
