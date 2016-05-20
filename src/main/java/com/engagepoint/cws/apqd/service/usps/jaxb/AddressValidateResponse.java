package com.engagepoint.cws.apqd.service.usps.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by dmytro.palczewski on 2/12/2016.
 */
@XmlRootElement(name = "AddressValidateResponse" )
@XmlAccessorType(XmlAccessType.FIELD)
public class AddressValidateResponse {

    @XmlElement(name="Address")
    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
