package com.engagepoint.cws.apqd.config.zabbix;
/**
 * Created by dmytro.palczewski on 6/3/2016.
 */
public class ActiveChecksRequest extends ApqdZabbixRequest {

    public static final String ACTIVE_CHECKS_REQUEST_NAME = "active checks";

    private String host;

    private String hostMetadata;

    public ActiveChecksRequest(){
        super(ACTIVE_CHECKS_REQUEST_NAME);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHostMetadata() {
        return hostMetadata;
    }

    public void setHostMetadata(String hostMetadata) {
        this.hostMetadata = hostMetadata;
    }
}
