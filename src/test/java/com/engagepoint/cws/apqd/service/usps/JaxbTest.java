package com.engagepoint.cws.apqd.service.usps;

import com.engagepoint.cws.apqd.service.usps.jaxb.Address;
import com.engagepoint.cws.apqd.service.usps.jaxb.AddressValidateRequest;
import com.engagepoint.cws.apqd.service.usps.jaxb.AddressValidateResponse;
import com.engagepoint.cws.apqd.service.usps.jaxb.Error;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import static org.junit.Assert.assertEquals;

/**
 * Created by dmytro.palczewski on 2/12/2016.
 */
public class JaxbTest {

    private static JAXBContext jaxbContext;
    private static Marshaller marshaller;
    private static Unmarshaller unmarshaller;

    @BeforeClass
    public static void beforeClass() throws Exception{
        jaxbContext = JAXBContext.newInstance( AddressValidateResponse.class, Address.class, Error.class, AddressValidateRequest.class );
        marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        unmarshaller = jaxbContext.createUnmarshaller();
    }

    @Test
    public void testRequest() throws Exception {

        Address address = new Address();
        address.setId("0");
        address.setFirmName("Awesome Inc.");
        address.setAddress1("110");
        address.setAddress2("3901 Calverton Blvd.");
        address.setCity("Calverton");
        address.setState("MD");
        address.setUrbanization("urbanization");
        address.setZip5("20705");
        address.setZip4("1111");
        address.setDeliveryPoint("office");
        address.setCarrierRoute("left, left, right, left, right");
        address.setReturnText("no return");

        AddressValidateRequest addressValidateRequestIn = new AddressValidateRequest();
        addressValidateRequestIn.setAddress(address);
        addressValidateRequestIn.setUserId("286ENGAG5998");
        addressValidateRequestIn.setIncludeOptionalElements(false);
        addressValidateRequestIn.setReturnCarrierRoute(true);

        String xml = JaxbUtils.marshal(addressValidateRequestIn, marshaller);
//        System.out.println(xml);

        AddressValidateRequest addressValidateRequestOut =  (AddressValidateRequest)JaxbUtils.unmarshal(xml, unmarshaller);
        assertEquals(address.getId(), addressValidateRequestOut.getAddress().getId());
        assertEquals(address.getFirmName(), addressValidateRequestOut.getAddress().getFirmName());
        assertEquals(address.getAddress1(), addressValidateRequestOut.getAddress().getAddress1());
        assertEquals(address.getAddress2(), addressValidateRequestOut.getAddress().getAddress2());
        assertEquals(address.getCity(), addressValidateRequestOut.getAddress().getCity());
        assertEquals(address.getState(), addressValidateRequestOut.getAddress().getState());
        assertEquals(address.getUrbanization(), addressValidateRequestOut.getAddress().getUrbanization());
        assertEquals(address.getZip4(), addressValidateRequestOut.getAddress().getZip4());
        assertEquals(address.getZip5(), addressValidateRequestOut.getAddress().getZip5());
        assertEquals(address.getDeliveryPoint(), addressValidateRequestOut.getAddress().getDeliveryPoint());
        assertEquals(address.getCarrierRoute(), addressValidateRequestOut.getAddress().getCarrierRoute());
        assertEquals(address.getReturnText(), addressValidateRequestOut.getAddress().getReturnText());
    }

    @Test
    public void testError() throws Exception {
        Error error = new Error();
        error.setNumber("-2147219402");
        error.setSource("clsAMS");
        error.setDescription("Invalid State Code.");
        error.setHelpFile("");
        error.setHelpContext("");

        Address address = new Address();
        address.setId("0");
        address.setError(error);

        AddressValidateResponse addressValidateResponseIn = new AddressValidateResponse();
        addressValidateResponseIn.setAddress(address);

        String xml = JaxbUtils.marshal(addressValidateResponseIn, marshaller);
//        System.out.println(xml);

        AddressValidateResponse addressValidateResponseOut =  (AddressValidateResponse)JaxbUtils.unmarshal(xml, unmarshaller);
        assertEquals("Invalid State Code.", addressValidateResponseOut.getAddress().getError().getDescription());
    }
}
