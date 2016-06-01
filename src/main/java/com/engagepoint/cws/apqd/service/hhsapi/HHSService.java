package com.engagepoint.cws.apqd.service.hhsapi;

import com.engagepoint.cws.apqd.config.HHSAPIProperties;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class HHSService {

    @Autowired
    private HHSAPIProperties hhsapiProperties;

    private HttpRequestFactory requestFactory;

    @PostConstruct
    public void init() {
        requestFactory = new NetHttpTransport().createRequestFactory();
    }

    public String findFosterFamilyAgencies(String queryString) throws URISyntaxException, IOException {
        StringBuilder url = new StringBuilder(hhsapiProperties.getHhsApiUrl());
        if(StringUtils.isNotBlank(queryString)) {
            url.append("?").append(queryString);
        }
        URIBuilder uriBuilder = new URIBuilder(url.toString());
        GenericUrl genericUrl = new GenericUrl(uriBuilder.build());
        HttpRequest httpRequest = requestFactory.buildGetRequest(genericUrl);
        return httpRequest.execute().parseAsString();
    }
}
