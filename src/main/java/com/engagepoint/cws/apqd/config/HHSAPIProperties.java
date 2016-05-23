package com.engagepoint.cws.apqd.config;

/**
 * Created by dmitry.rudenko on 5/23/2016.
 */

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "hhs-api")
public class HHSAPIProperties {
    private String hhsApiUrl;

    public String getHhsApiUrl() {
        return hhsApiUrl;
    }

    public void setHhsApiUrl(String hhsApiUrl) {
        this.hhsApiUrl = hhsApiUrl;
    }
}
