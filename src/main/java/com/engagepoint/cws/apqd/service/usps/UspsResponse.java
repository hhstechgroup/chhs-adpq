package com.engagepoint.cws.apqd.service.usps;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by dmytro.palczewski on 2/11/2016.
 */
public class UspsResponse {

    private UspsResponseType responseType;
    private UspsAddress address;
    private String errorDescription;
    private String returnText;

    public UspsResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(UspsResponseType responseType) {
        this.responseType = responseType;
    }

    public UspsAddress getAddress() {
        return address;
    }

    public void setAddress(UspsAddress address) {
        this.address = address;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getReturnText() {
        return returnText;
    }

    public void setReturnText(String returnText) {
        this.returnText = returnText;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).
            append("ResponseType", responseType).
            append("Address", address).
            append("ErrorDescription", errorDescription).
            append("ReturnText", returnText).
            toString();
    }
}
