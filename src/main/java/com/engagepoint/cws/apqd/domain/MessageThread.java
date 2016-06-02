package com.engagepoint.cws.apqd.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Document(indexName = "messagethread")
public class MessageThread {

    private static AtomicLong seqience = new AtomicLong();

    public MessageThread() {
        this.id = seqience.addAndGet(1);
    }

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
