package com.engagepoint.cws.apqd.service.usps.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by dmytro.palczewski on 2/12/2016.
 */
@XmlRootElement(name = "Error")
@XmlAccessorType(XmlAccessType.FIELD)
public class Error {

    @XmlElement(name = "Number")
    private String number;
    @XmlElement(name = "Source")
    private String source;
    @XmlElement(name = "Description")
    private String description;
    @XmlElement(name = "HelpFile")
    private String helpFile;
    @XmlElement(name = "HelpContext")
    private String helpContext;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHelpFile() {
        return helpFile;
    }

    public void setHelpFile(String helpFile) {
        this.helpFile = helpFile;
    }

    public String getHelpContext() {
        return helpContext;
    }

    public void setHelpContext(String helpContext) {
        this.helpContext = helpContext;
    }


}
