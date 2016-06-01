package com.engagepoint.cws.apqd.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.List;

@Document(indexName = "messagethread")
public class MessageThread {

    private Long id;

    private List<Message> thread = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Message> getThread() {
        return thread;
    }

    public void setThread(List<Message> thread) {
        this.thread = thread;
    }

    public void addMessage(Message message) {
        thread.add(message);
    }
}
