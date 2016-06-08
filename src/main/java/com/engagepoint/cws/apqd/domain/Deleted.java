package com.engagepoint.cws.apqd.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Deleted.
 */
@Entity
@Table(name = "deleted")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "deleted")
public class Deleted implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Message message;

    @OneToOne
    private User deletedBy;

    @Column(name = "deleted_date")
    private ZonedDateTime deletedDate;

    public void setMessage(Message message) {
        this.message = message;
    }

    public ZonedDateTime getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(ZonedDateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    public User getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(User deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessages(Message message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Deleted deleted = (Deleted) o;
        if(deleted.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, deleted.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Deleted{" +
            "id=" + id +
            '}';
    }
}
