package com.engagepoint.cws.apqd.config.zabbix;

/**
 * Created by dmytro.palczewski on 6/6/2016.
 */
public class ZabbixActiveRequestSenderTestDriver {
    public static void main(String[] args)  throws Exception  {

        ApqdZabbixSender zabbixActiveRequestSender = new ApqdZabbixSender("mdc-apdq-inf-a1.engagepoint.us", 10051);
        ActiveChecksRequest activeChecksRequest = new ActiveChecksRequest();
        activeChecksRequest.setHost("apdq-4");
        activeChecksRequest.setHostMetadata("apdq-cluster");
        zabbixActiveRequestSender.send(activeChecksRequest);
    }
}
