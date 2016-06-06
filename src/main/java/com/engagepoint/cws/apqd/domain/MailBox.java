package com.engagepoint.cws.apqd.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A MailBox.
 */
@Entity
@Table(name = "mail_box")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "mailbox")
public class MailBox implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Inbox inbox;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Outbox outbox;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Draft draft;

    @OneToOne
    @JsonIgnore
    private User user;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "user_contacts",
        joinColumns = {@JoinColumn(name = "mail_box_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    private Set<User> contacts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Inbox getInbox() {
        return inbox;
    }

    public void setInbox(Inbox inbox) {
        this.inbox = inbox;
    }

    public Outbox getOutbox() {
        return outbox;
    }

    public void setOutbox(Outbox outbox) {
        this.outbox = outbox;
    }

    public Set<User> getContacts() {
        return contacts;
    }

    public void setContacts(Set<User> users) {
        this.contacts = users;
    }

    public Draft getDraft() {
        return draft;
    }

    public void setDraft(Draft draft) {
        this.draft = draft;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MailBox mailBox = (MailBox) o;
        if(mailBox.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, mailBox.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MailBox{" +
            "id=" + id +
            '}';
    }
}
