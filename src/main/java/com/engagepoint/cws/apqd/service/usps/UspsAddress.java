package com.engagepoint.cws.apqd.service.usps;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by dmytro.palczewski on 2/11/2016.
 */
public class UspsAddress {

    private String streetAddress;
    private String apartmentOrSuite;
    private String city;
    private String state;
    private String zip;
    private String zip4;

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getApartmentOrSuite() {
        return apartmentOrSuite;
    }

    public void setApartmentOrSuite(String apartmentOrSuite) {
        this.apartmentOrSuite = apartmentOrSuite;
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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getZip4() {
        return zip4;
    }

    public void setZip4(String zip4) {
        this.zip4 = zip4;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
            append("StreetAddress", streetAddress).
            append("ApartmentOrSuite", apartmentOrSuite).
            append("City", city).
            append("State", state).
            append("Zip", zip).
            append("Zip4", zip4).
            toString();
    }
}
