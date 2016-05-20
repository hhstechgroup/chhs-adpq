package com.engagepoint.cws.apqd.service.usps.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Created by dmytro.palczewski on 2/12/2016.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Address {

    @XmlAttribute(name="ID")
    private String id;

    @XmlElement(name="FirmName")
    private String firmName;

    @XmlElement(name="Address1")
    private String address1;

    @XmlElement(name="Address2")
    private String address2;

    @XmlElement(name="City")
    private String city;

    @XmlElement(name="State")
    private String state;

    @XmlElement(name="Urbanization")
    private String urbanization;

    @XmlElement(name="Zip5")
    private String zip5;

    @XmlElement(name="Zip4")
    private String zip4;

    @XmlElement(name="DeliveryPoint")
    private String deliveryPoint;

    @XmlElement(name="CarrierRoute")
    private String carrierRoute;

    @XmlElement(name="ReturnText")
    private String returnText;

    @XmlElement(name="Error")
    private Error error;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUrbanization() {
        return urbanization;
    }

    public void setUrbanization(String urbanization) {
        this.urbanization = urbanization;
    }

    public String getZip5() {
        return zip5;
    }

    public void setZip5(String zip5) {
        this.zip5 = zip5;
    }

    public String getZip4() {
        return zip4;
    }

    public void setZip4(String zip4) {
        this.zip4 = zip4;
    }

    public String getDeliveryPoint() {
        return deliveryPoint;
    }

    public void setDeliveryPoint(String deliveryPoint) {
        this.deliveryPoint = deliveryPoint;
    }

    public String getCarrierRoute() {
        return carrierRoute;
    }

    public void setCarrierRoute(String carrierRoute) {
        this.carrierRoute = carrierRoute;
    }

    public String getReturnText() {
        return returnText;
    }

    public void setReturnText(String returnText) {
        this.returnText = returnText;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
