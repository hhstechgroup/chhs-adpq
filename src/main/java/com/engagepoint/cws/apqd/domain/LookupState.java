package com.engagepoint.cws.apqd.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A LookupState.
 */
@Entity
@Table(name = "lookup_state")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lookupstate")
public class LookupState implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 2)
    @Column(name = "state_code", length = 2, nullable = false)
    private String stateCode;
    
    @NotNull
    @Column(name = "state_name", nullable = false)
    private String stateName;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStateCode() {
        return stateCode;
    }
    
    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }
    
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LookupState lookupState = (LookupState) o;
        if(lookupState.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lookupState.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LookupState{" +
            "id=" + id +
            ", stateCode='" + stateCode + "'" +
            ", stateName='" + stateName + "'" +
            '}';
    }
}
