package com.engagepoint.cws.apqd.config.zabbix;

/**
 * Created by dmytro.palczewski on 6/3/2016.
 */
public final class ZabbixConstants {

    private ZabbixConstants(){
        // nothing to do
    }

    private static final int DATALEN_HEADER_LENGTH = 8;
    public static final byte[] ZABBIX_HEADER = { 'Z', 'B', 'X', 'D', '\1' };
    public static final int HEADERS_LENGTH = ZABBIX_HEADER.length + DATALEN_HEADER_LENGTH;//13
    public static final String RESPONSE_FLAG_KEY = "response";
    public static final String SUCCESS_RESPONSE_FLAG = "success";
    public static final String SUCCESS_FAILED_FLAG = "failed";
}
