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
 * A LookupCounty.
 */
@Entity
@Table(name = "lookup_county")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lookupcounty")
public class LookupCounty implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 40)
    @Column(name = "county_name", length = 40, nullable = false)
    private String countyName;
    
    @OneToOne
    private LookupState state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }
    
    public void setCountyName(String countyName) {
        this.countyName = countyName;
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
        LookupCounty lookupCounty = (LookupCounty) o;
        if(lookupCounty.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lookupCounty.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LookupCounty{" +
            "id=" + id +
            ", countyName='" + countyName + "'" +
            '}';
    }
}
