package com.engagepoint.cws.apqd.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A LookupMaritalStatus.
 */
@Entity
@Table(name = "lookup_marital_status")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lookupmaritalstatus")
public class LookupMaritalStatus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "marital_status_name", nullable = false)
    private String maritalStatusName;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaritalStatusName() {
        return maritalStatusName;
    }
    
    public void setMaritalStatusName(String maritalStatusName) {
        this.maritalStatusName = maritalStatusName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LookupMaritalStatus lookupMaritalStatus = (LookupMaritalStatus) o;
        if(lookupMaritalStatus.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lookupMaritalStatus.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LookupMaritalStatus{" +
            "id=" + id +
            ", maritalStatusName='" + maritalStatusName + "'" +
            '}';
    }
}
