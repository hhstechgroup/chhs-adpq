package com.engagepoint.cws.apqd.config.zabbix;

import com.alibaba.fastjson.JSON;

import static com.engagepoint.cws.apqd.config.zabbix.ZabbixConstants.HEADERS_LENGTH;
import static com.engagepoint.cws.apqd.config.zabbix.ZabbixConstants.ZABBIX_HEADER;

/**
 * Created by dmytro.palczewski on 6/6/2016.
 */
public abstract class ApqdZabbixRequest {

    private String request;

    public ApqdZabbixRequest(String request){
        this.request = request;
    }

    public String getRequest() {
        return request;
    }

    public byte[] toBytes() {
        byte[] jsonBytes = JSON.toJSONBytes(this);

        byte[] result = new byte[HEADERS_LENGTH + jsonBytes.length];

        System.arraycopy(ZABBIX_HEADER, 0, result, 0, ZABBIX_HEADER.length);

        result[ZABBIX_HEADER.length] = (byte) (jsonBytes.length & 0xFF);
        result[ZABBIX_HEADER.length + 1] = (byte) ((jsonBytes.length >> 8) & 0x00FF);
        result[ZABBIX_HEADER.length + 2] = (byte) ((jsonBytes.length >> 16) & 0x0000FF);
        result[ZABBIX_HEADER.length + 3] = (byte) ((jsonBytes.length >> 24) & 0x000000FF);

        System.arraycopy(jsonBytes, 0, result, HEADERS_LENGTH, jsonBytes.length);

        return result;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
