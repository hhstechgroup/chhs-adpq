package com.engagepoint.cws.apqd.service.hhsapi;

import com.engagepoint.cws.apqd.config.HHSAPIProperties;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
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
    private static final String WHERE_CLAUSE = "within_box(location, %1.6f, %2.6f, %3.6f, %4.6f)";

    @PostConstruct
    public void init() {
        requestFactory = new NetHttpTransport().createRequestFactory();
    }

    public String findFosterFamilyAgencies(double nwLatitude, double nwLongitude, double seLatitude, double seLongitude) throws URISyntaxException, IOException {
        URIBuilder uriBuilder = new URIBuilder(hhsapiProperties.getHhsApiUrl());
        uriBuilder.addParameter("$where", String.format(WHERE_CLAUSE, nwLatitude, nwLongitude, seLatitude, seLongitude));
        GenericUrl genericUrl = new GenericUrl(uriBuilder.build());
        HttpRequest httpRequest = requestFactory.buildGetRequest(genericUrl);
        return httpRequest.execute().parseAsString();
    }
}
