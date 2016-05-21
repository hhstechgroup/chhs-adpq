package com.engagepoint.cws.apqd.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Phone.
 */
@Entity
@Table(name = "phone")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "phone")
public class Phone implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 15)
    @Column(name = "phone_number", length = 15, nullable = false)
    private String phoneNumber;
    
    @Column(name = "preferred")
    private Boolean preferred;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getPreferred() {
        return preferred;
    }
    
    public void setPreferred(Boolean preferred) {
        this.preferred = preferred;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Phone phone = (Phone) o;
        if(phone.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, phone.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Phone{" +
            "id=" + id +
            ", phoneNumber='" + phoneNumber + "'" +
            ", preferred='" + preferred + "'" +
            ", startDate='" + startDate + "'" +
            ", endDate='" + endDate + "'" +
            '}';
    }
}
