package com.engagepoint.cws.apqd.domain;

public class MessageThreadDeletedItem {

    private String deletedBy;

    private Message message;

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageThreadDeletedItem that = (MessageThreadDeletedItem) o;

        if (deletedBy != null ? !deletedBy.equals(that.deletedBy) : that.deletedBy != null) return false;
        return message != null ? message.equals(that.message) : that.message == null;

    }

    @Override
    public int hashCode() {
        int result = deletedBy != null ? deletedBy.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }
}
