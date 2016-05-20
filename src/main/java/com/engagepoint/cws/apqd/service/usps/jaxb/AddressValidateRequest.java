package com.engagepoint.cws.apqd.service.usps.jaxb;

import javax.xml.bind.annotation.*;

/**
 * Created by dmytro.palczewski on 2/12/2016.
 */
@XmlRootElement(name = "AddressValidateRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class AddressValidateRequest {

    @XmlAttribute(name="USERID")
    private String userId;

    @XmlElement(name="IncludeOptionalElements")
    private boolean includeOptionalElements;

    @XmlElement(name="ReturnCarrierRoute")
    private boolean returnCarrierRoute;

    @XmlElement(name="Address")
    private Address address;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isIncludeOptionalElements() {
        return includeOptionalElements;
    }

    public void setIncludeOptionalElements(boolean includeOptionalElements) {
        this.includeOptionalElements = includeOptionalElements;
    }

    public boolean isReturnCarrierRoute() {
        return returnCarrierRoute;
    }

    public void setReturnCarrierRoute(boolean returnCarrierRoute) {
        this.returnCarrierRoute = returnCarrierRoute;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
