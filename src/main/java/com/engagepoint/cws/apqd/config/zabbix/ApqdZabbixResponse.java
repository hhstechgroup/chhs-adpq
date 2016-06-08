package com.engagepoint.cws.apqd.config.zabbix;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by dmytro.palczewski on 6/6/2016.
 */
public class ApqdZabbixResponse {

    public static final ApqdZabbixResponse EMPTY_RESPONSE = new ApqdZabbixResponse(ZabbixResponseType.EMPTY);

    private ZabbixResponseType type;

    private String stringValue;

    private JSONObject jsonValue;

    public ApqdZabbixResponse(ZabbixResponseType type){
        this.type = type;
    }

    public ApqdZabbixResponse(String stringValue){
        this.stringValue = stringValue;
    }

    public ZabbixResponseType getType() {
        return type;
    }

    public void setType(ZabbixResponseType type) {
        this.type = type;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public JSONObject getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(JSONObject jsonValue) {
        this.jsonValue = jsonValue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("type:");
        sb.append(type);

        if(stringValue != null){
            sb.append(", value:");
            sb.append(stringValue);
        }
        return sb.toString() ;
    }
}
