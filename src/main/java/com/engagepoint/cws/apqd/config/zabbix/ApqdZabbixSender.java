package com.engagepoint.cws.apqd.config.zabbix;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import static com.engagepoint.cws.apqd.config.zabbix.ZabbixConstants.*;
import static com.engagepoint.cws.apqd.config.zabbix.ZabbixResponseType.*;

/**
 * Created by dmytro.palczewski on 6/3/2016.
 */
public class ApqdZabbixSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApqdZabbixSender.class);

    String host;
    int port;
    int connectTimeout = 3 * 1000;
    int socketTimeout = 3 * 1000;

    public ApqdZabbixSender(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public ApqdZabbixSender(String host, int port, int connectTimeout, int socketTimeout) {
        this(host, port);
        this.connectTimeout = connectTimeout;
        this.socketTimeout = socketTimeout;
    }

    public ApqdZabbixResponse send(ApqdZabbixRequest request) throws IOException {

        LOGGER.info("Request {} is sending to Zabbix", request);
        byte[] responseData = send(request.toBytes());
        return createResponse(responseData);
    }

    private byte[] send (byte[] request) throws IOException {

        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            socket = new Socket();

            socket.setSoTimeout(socketTimeout);
            socket.connect(new InetSocketAddress(host, port), connectTimeout);

            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            outputStream.write(request);
            outputStream.flush();

            return IOUtils.toByteArray(inputStream);

        } finally {
            if (socket != null) {
                socket.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    static ApqdZabbixResponse createResponse(byte[] responseData){

        if (responseData == null || responseData.length <= HEADERS_LENGTH) {
            LOGGER.error("It seems that Zabbix server response is empty");
            return ApqdZabbixResponse.EMPTY_RESPONSE;
        }

        String jsonString = new String(responseData, HEADERS_LENGTH, responseData.length - HEADERS_LENGTH);
        LOGGER.info("Zabbix response:{}", jsonString);

        return createResponseFromJsonString(jsonString);
    }

    static ApqdZabbixResponse createResponseFromJsonString(String jsonString){

        ApqdZabbixResponse response = new ApqdZabbixResponse(jsonString);

        JSONObject jsonObject = JSON.parseObject(jsonString);
        response.setJsonValue(jsonObject);
        String responseTypeStr = jsonObject.getString(RESPONSE_FLAG_KEY);

        if(SUCCESS_RESPONSE_FLAG.equals(responseTypeStr)){
            response.setType(SUCCESS);
        } else if(SUCCESS_FAILED_FLAG.equals(responseTypeStr)){
            response.setType(FAILED);
        }else{
            response.setType(UNKNOWN);
            LOGGER.error("Zabbix has returned unknown response type: {}", responseTypeStr);
        }
        return response;
    }
}
