package com.engagepoint.cws.apqd.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A LookupGender.
 */
@Entity
@Table(name = "lookup_gender")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "lookupgender")
public class LookupGender implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "gender_code", length = 1, nullable = false)
    private String genderCode;
    
    @NotNull
    @Column(name = "gender_name", nullable = false)
    private String genderName;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGenderCode() {
        return genderCode;
    }
    
    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getGenderName() {
        return genderName;
    }
    
    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LookupGender lookupGender = (LookupGender) o;
        if(lookupGender.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, lookupGender.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "LookupGender{" +
            "id=" + id +
            ", genderCode='" + genderCode + "'" +
            ", genderName='" + genderName + "'" +
            '}';
    }
}
