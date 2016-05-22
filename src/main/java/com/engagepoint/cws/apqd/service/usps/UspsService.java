package com.engagepoint.cws.apqd.service.usps;

import com.engagepoint.cws.apqd.service.usps.jaxb.Address;
import com.engagepoint.cws.apqd.service.usps.jaxb.AddressValidateRequest;
import com.engagepoint.cws.apqd.service.usps.jaxb.AddressValidateResponse;
import com.engagepoint.cws.apqd.service.usps.jaxb.Error;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by dmytro.palczewski on 2/10/2ModelService016.
 */
@Service
public class UspsService {

    private static final Logger log = LoggerFactory.getLogger(UspsService.class);

    private String serverUrl;

    private String userId;

    private int timeout;

    private JAXBContext jaxbContext;

    private Marshaller marshaller;

    private Unmarshaller unmarshaller;

    @PostConstruct
    public void initialize() throws JAXBException {
        jaxbContext = JAXBContext.newInstance(
            AddressValidateRequest.class,
            AddressValidateResponse.class,
            Address.class,
            Error.class
        );
        marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        unmarshaller = jaxbContext.createUnmarshaller();
    }

    /*

    Address Standardization Samples: (API=Verify)

    Request:
    <AddressValidateRequest USERID="xxxxxxx" PASSWORD="xxxxxxx">
        <Address ID="0">
            <Address1></Address1>
            <Address2>6406 Ivy Lane</Address2>
            <City>Greenbelt</City>
            <State>MD</State>
            <Zip5></Zip5>
            <Zip4></Zip4>
        </Address>
    </AddressValidateRequest>

    Response:
    <AddressValidateResponse>
        <Address ID="0">
            <Address2>6406 IVY LN</Address2>
            <City>GREENBELT</City>
            <State>MD</State>
            <Zip5>20770</Zip5>
            <Zip4>1440</Zip4>
        </Address>
    </AddressValidateResponse>

    */
    public UspsResponse verifyAddress(UspsAddress inputAddress) {

        log.info("input address is :" + inputAddress);

        String inputState = inputAddress.getState();
        String inputCity = inputAddress.getCity();
        String inputZip = inputAddress.getZip();

        if ((StringUtils.isEmpty(inputState) && StringUtils.isEmpty(inputCity) && StringUtils.isEmpty(inputZip)) ||    // No state, city or zip5
            (StringUtils.isEmpty(inputZip) && (StringUtils.isEmpty(inputState) || StringUtils.isEmpty(inputCity)))) {  // Both state and city are required if no zip5
            String msg = "USPS address validation requires either zip or city and state";
            log.error(msg);
            return errorResponse(msg);
        }

        AddressValidateRequest uspsXmlRequest = createUspsXmlRequest(inputAddress);

        AddressValidateResponse uspsXmlResponse = null;
        try {
            uspsXmlResponse = sendUspsRequest("Verify", uspsXmlRequest);
        } catch (UspsRequestException e) {
            log.info(e.getMessage());
            return errorResponse("Error sending request for USPS Address Validation service:"  + e.getMessage());
        }

        Address responseXmlAddress = uspsXmlResponse.getAddress();
        if (responseXmlAddress == null) {
            return errorResponse("Incomplete response from USPS Address Validation service: no Address element found");
        }

        Error responseXmlError = responseXmlAddress.getError();
        if (responseXmlError != null) {
            return errorResponse(responseXmlError.getDescription());
        }

        UspsResponse response = new UspsResponse();
        UspsAddress outputAddress = new UspsAddress();

        // Note: an Address1 element is not returned if empty
        String address1 = responseXmlAddress.getAddress1();
        if (StringUtils.isNotEmpty(address1)) {
            outputAddress.setApartmentOrSuite(address1);
        }
        outputAddress.setStreetAddress(responseXmlAddress.getAddress2());
        outputAddress.setCity(responseXmlAddress.getCity());
        outputAddress.setState(responseXmlAddress.getState());
        outputAddress.setZip(responseXmlAddress.getZip5());
        outputAddress.setZip4(responseXmlAddress.getZip4());

        String returnText = responseXmlAddress.getReturnText();
        if (returnText != null) {
            response.setReturnText(returnText);
        }
        response.setResponseType(UspsResponseType.SUCCESS);
        response.setAddress(outputAddress);
        log.info("response :" + response);
        return response;
    }

    private AddressValidateRequest createUspsXmlRequest(UspsAddress inputAddress) {
        AddressValidateRequest addressValidateRequest = new AddressValidateRequest();

        addressValidateRequest.setUserId(userId);

        Address address = new Address();
        address.setId("0");
        address.setAddress1(emptyIfNull(inputAddress.getApartmentOrSuite()));
        address.setAddress2(emptyIfNull(inputAddress.getStreetAddress()));
        address.setCity(emptyIfNull(inputAddress.getCity()));
        address.setState(emptyIfNull(inputAddress.getState()));
        address.setZip5(emptyIfNull(inputAddress.getZip()));
        address.setZip4(emptyIfNull(inputAddress.getZip4()));

        addressValidateRequest.setAddress(address);

        return addressValidateRequest;
    }

    private static String emptyIfNull(String value){
        return value == null ? "" : value;
    }

    private AddressValidateResponse sendUspsRequest(String requestType, AddressValidateRequest addressValidateRequest) throws UspsRequestException {

        String xmlRequestString = null;
        try {
            xmlRequestString = JaxbUtils.marshal(addressValidateRequest, marshaller);
        } catch (Exception e) {
            throw new UspsRequestException("Error serializing requestDocument:"  + e.getMessage());
        }
        log.info("USPS XML request string: " + xmlRequestString);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        String responseXmlString = null;

        try {
            URI uri = createHttpGetUri(requestType, xmlRequestString);
            HttpGet httpGet = new HttpGet(uri);
            configureHttpGet(httpGet);

            httpResponse = httpClient.execute(httpGet);
            log.info("Response Code : {}", httpResponse.getStatusLine().getStatusCode());

            HttpEntity httpEntity = httpResponse.getEntity();
            responseXmlString = getHttpResponseString(httpEntity);
            EntityUtils.consume(httpEntity);

        } catch (Exception e) {
            throw new UspsRequestException("USPS Shipment Gateway Configuration is not available: " + e.getMessage());
        }finally {
            try {
                if (httpResponse != null) httpResponse.close();
            }catch (IOException e) {
                log.error("cannot close properly CloseableHttpResponse", e);
            }
        }

        log.info("USPS response: " + responseXmlString);

        if (StringUtils.isEmpty(responseXmlString)) {
            return null;
        }

        Object xmlResponseObj = null;
        try {
            xmlResponseObj = JaxbUtils.unmarshal(responseXmlString, unmarshaller);
        } catch (Exception e) {
            throw new UspsRequestException("Error reading response Document from a String: " + e.getMessage());
        }

        // If a top-level error document is returned, throw exception
        // Other request-level errors should be handled by the caller
        if (xmlResponseObj instanceof Error) {
            Error xmlResponseError = (Error)xmlResponseObj;
            throw new UspsRequestException(xmlResponseError.getDescription());
        }

        return (AddressValidateResponse)xmlResponseObj;
    }

    private URI createHttpGetUri(String requestType, String xmlString) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(serverUrl);
        uriBuilder.addParameter("API", requestType);
        uriBuilder.addParameter("XML", xmlString);
        URI uri = uriBuilder.build();
        log.info("request uri = [{}]", uri.toString());
        return uri;
    }

    private void configureHttpGet(HttpGet httpGet) {
        int timeoutMilli = timeout * 1000;
        RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(timeoutMilli)
            .setConnectTimeout(timeoutMilli)
            .setConnectionRequestTimeout(timeoutMilli)
            .build();
        httpGet.setConfig(requestConfig);
    }

    private String getHttpResponseString(HttpEntity httpEntity) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(httpEntity.getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        String responseString = result.toString();
        return responseString;
    }

    private static UspsResponse errorResponse(String message){
        UspsResponse response = new UspsResponse();
        response.setResponseType(UspsResponseType.ERROR);
        response.setErrorDescription(message);
        return response;
    }

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
