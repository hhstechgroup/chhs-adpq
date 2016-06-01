package com.engagepoint.cws.apqd.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Place.
 */
@Entity
@Table(name = "place")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "place")
public class Place implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 10)
    @Column(name = "unit_number", length = 10)
    private String unitNumber;
    
    @Size(max = 20)
    @Column(name = "city_name", length = 20)
    private String cityName;
    
    @NotNull
    @Size(max = 100)
    @Column(name = "street_name", length = 100, nullable = false)
    private String streetName;
    
    @Size(max = 10)
    @Column(name = "street_number", length = 10)
    private String streetNumber;
    
    @Size(max = 5)
    @Column(name = "zip_code", length = 5)
    private String zipCode;
    
    @Size(max = 4)
    @Column(name = "zip_suffix", length = 4)
    private String zipSuffix;
    
    @Column(name = "longitude")
    private Double longitude;
    
    @Column(name = "latitude")
    private Double latitude;
    
    @Column(name = "valid_address_flag")
    private Boolean validAddressFlag;
    
    @Column(name = "validation_status")
    private String validationStatus;
    
    @Column(name = "validation_message")
    private String validationMessage;
    
    @ManyToOne
    @JoinColumn(name = "county_id")
    private LookupCounty county;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private LookupState state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitNumber() {
        return unitNumber;
    }
    
    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getCityName() {
        return cityName;
    }
    
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStreetName() {
        return streetName;
    }
    
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }
    
    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getZipCode() {
        return zipCode;
    }
    
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getZipSuffix() {
        return zipSuffix;
    }
    
    public void setZipSuffix(String zipSuffix) {
        this.zipSuffix = zipSuffix;
    }

    public Double getLongitude() {
        return longitude;
    }
    
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }
    
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Boolean getValidAddressFlag() {
        return validAddressFlag;
    }
    
    public void setValidAddressFlag(Boolean validAddressFlag) {
        this.validAddressFlag = validAddressFlag;
    }

    public String getValidationStatus() {
        return validationStatus;
    }
    
    public void setValidationStatus(String validationStatus) {
        this.validationStatus = validationStatus;
    }

    public String getValidationMessage() {
        return validationMessage;
    }
    
    public void setValidationMessage(String validationMessage) {
        this.validationMessage = validationMessage;
    }

    public LookupCounty getCounty() {
        return county;
    }

    public void setCounty(LookupCounty lookupCounty) {
        this.county = lookupCounty;
    }

    public LookupState getState() {
        return state;
    }

    public void setState(LookupState lookupState) {
        this.state = lookupState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Place place = (Place) o;
        if(place.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, place.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Place{" +
            "id=" + id +
            ", unitNumber='" + unitNumber + "'" +
            ", cityName='" + cityName + "'" +
            ", streetName='" + streetName + "'" +
            ", streetNumber='" + streetNumber + "'" +
            ", zipCode='" + zipCode + "'" +
            ", zipSuffix='" + zipSuffix + "'" +
            ", longitude='" + longitude + "'" +
            ", latitude='" + latitude + "'" +
            ", validAddressFlag='" + validAddressFlag + "'" +
            ", validationStatus='" + validationStatus + "'" +
            ", validationMessage='" + validationMessage + "'" +
            '}';
    }
}
