package com.engagepoint.cws.apqd.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.engagepoint.cws.apqd.domain.enumeration.MessageStatus;

/**
 * A Message.
 */
@Entity
@Table(name = "message")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "message")
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 2000)
    @Column(name = "body", length = 2000, nullable = false)
    private String body;

    @NotNull
    @Size(max = 100)
    @Column(name = "subject", length = 100, nullable = false)
    private String subject;

    @Size(max = 20)
    @Column(name = "case_number", length = 20)
    private String caseNumber;

    @Column(name = "date_created")
    private ZonedDateTime dateCreated;

    @Column(name = "date_read")
    private ZonedDateTime dateRead;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private MessageStatus status;

    @OneToOne
    private Message replyOn;

    @OneToOne
    private User to;

    @OneToOne
    private User from;

    @ManyToOne
    @JoinColumn(name = "inbox_id")
    private Inbox inbox;

    @ManyToOne
    @JoinColumn(name = "outbox_id")
    private Outbox outbox;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public ZonedDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(ZonedDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public ZonedDateTime getDateRead() {
        return dateRead;
    }

    public void setDateRead(ZonedDateTime dateRead) {
        this.dateRead = dateRead;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public Message getReplyOn() {
        return replyOn;
    }

    public void setReplyOn(Message message) {
        this.replyOn = message;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User user) {
        this.to = user;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User user) {
        this.from = user;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        if(message.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Message{" +
            "id=" + id +
            ", body='" + body + "'" +
            ", subject='" + subject + "'" +
            ", caseNumber='" + caseNumber + "'" +
            ", dateCreated='" + dateCreated + "'" +
            ", dateRead='" + dateRead + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
