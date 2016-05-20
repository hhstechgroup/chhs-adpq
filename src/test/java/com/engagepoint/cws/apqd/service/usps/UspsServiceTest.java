package com.engagepoint.cws.apqd.service.usps;

import com.engagepoint.cws.apqd.Application;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by dmytro.palczewski on 2/11/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Ignore
public class UspsServiceTest {

    @Inject
    private UspsService uspsService;

    @Test
    public void validateSuccess() {
        UspsAddress request = new UspsAddress();

        request.setStreetAddress("3901 Calverton blvd");
        request.setApartmentOrSuite("110");
        request.setCity("Calverton");
        request.setState("MD");
        request.setZip("20705");
        request.setZip("20705");
        request.setZip4("");

        UspsResponse response =  uspsService.verifyAddress(request);
        assertNotNull(response);
        assertEquals(UspsResponseType.SUCCESS, response.getResponseType());
        assertNull(response.getErrorDescription());
        assertNull(response.getReturnText());

        UspsAddress responseAddress = response.getAddress();
        assertNotNull(responseAddress);
        assertEquals("3901 CALVERTON BLVD", responseAddress.getStreetAddress());
        assertEquals("STE 110", responseAddress.getApartmentOrSuite());
        assertEquals("CALVERTON", responseAddress.getCity());
        assertEquals("MD", responseAddress.getState());
        assertEquals("20705", responseAddress.getZip());
        assertEquals("3415", responseAddress.getZip4());
    }

    @Test
    public void validateSuccessWithoutSuite() {
        UspsAddress request = new UspsAddress();

        request.setStreetAddress("3901 Calverton blvd");
        request.setCity("Calverton");
        request.setState("MD");
        request.setZip("20705");

        UspsResponse response =  uspsService.verifyAddress(request);
        assertNotNull(response);
        assertEquals(UspsResponseType.SUCCESS, response.getResponseType());
        assertNull(response.getErrorDescription());

        String returnText = response.getReturnText();
        assertNotNull(returnText);
        assertEquals("Default address: The address you entered was found but more information is needed " +
            "(such as an apartment, suite, or box number) to match to a specific address.", returnText.trim());

        UspsAddress responseAddress = response.getAddress();
        assertNotNull(responseAddress);
        assertEquals("3901 CALVERTON BLVD", responseAddress.getStreetAddress());
        assertNull(responseAddress.getApartmentOrSuite());
        assertEquals("CALVERTON", responseAddress.getCity());
        assertEquals("MD", responseAddress.getState());
        assertEquals("20705", responseAddress.getZip());
        assertEquals("3407", responseAddress.getZip4());
    }

    @Test
    public void validateError() {
        UspsAddress request = new UspsAddress();

        request.setStreetAddress("3901 Calverton blvd");
        request.setApartmentOrSuite("110");
        request.setCity("Calverton");
        request.setState("MM");
        request.setZip("11111");

        UspsResponse response =  uspsService.verifyAddress(request);
        assertNotNull(response);

        assertEquals(UspsResponseType.ERROR, response.getResponseType());
        String errorDescription = response.getErrorDescription();
        assertNotNull(response.getErrorDescription());
        assertEquals("Invalid Zip Code.", errorDescription.trim());

        assertNull(response.getReturnText());
        assertNull(response.getAddress());
    }

    @Test
    public void validateInvalidInput() {
        UspsAddress request = new UspsAddress();

        request.setStreetAddress("3901 Calverton blvd");
        request.setApartmentOrSuite("110");
        request.setCity("Calverton");

        UspsResponse response =  uspsService.verifyAddress(request);
        assertNotNull(response);

        assertEquals(UspsResponseType.ERROR, response.getResponseType());
        String errorDescription = response.getErrorDescription();
        assertNotNull(response.getErrorDescription());
        assertEquals("USPS address validation requires either zip or city and state", errorDescription.trim());

        assertNull(response.getReturnText());
        assertNull(response.getAddress());
    }

    @Test
    public void validateMultiply() {
        UspsAddress request = new UspsAddress();

        request.setStreetAddress("123 13TH ST NW");
        request.setApartmentOrSuite("110");
        request.setCity("WASHINGTON");
        request.setState("DC");
        request.setZip("20010");
        request.setZip4("2408");

        UspsResponse response =  uspsService.verifyAddress(request);
        assertNotNull(response);

        assertEquals(UspsResponseType.ERROR, response.getResponseType());
        String errorDescription = response.getErrorDescription();
        assertNotNull(response.getErrorDescription());
        assertEquals("Multiple addresses were found for the information you entered, and no default exists.", errorDescription.trim());

        assertNull(response.getReturnText());
        assertNull(response.getAddress());
    }
}
