package com.engagepoint.cws.apqd.web.websocket;

import com.engagepoint.cws.apqd.domain.Message;
import com.engagepoint.cws.apqd.domain.User;
import com.engagepoint.cws.apqd.domain.enumeration.MessageStatus;
import com.engagepoint.cws.apqd.repository.MessageRepository;
import com.engagepoint.cws.apqd.repository.UserRepository;
import com.engagepoint.cws.apqd.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MailBoxService implements ApplicationListener<SessionSubscribeEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailBoxService.class);

    public static final String TOPIC_MAIL_INBOX = "/topic/mail/inbox";
    public static final String TOPIC_MAIL_OUTBOX = "/topic/mail/outbox";
    public static final String TOPIC_MAIL_SEND = "/topic/mail/send";
    public static final String TOPIC_MAIL_CONFIRM = "/topic/mail/confirm";

    @Inject
    private UserRepository userRepository;

    @Inject
    private MessageRepository messageRepository;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @SubscribeMapping(TOPIC_MAIL_SEND)
    public void sendMessage(@Payload Message message) {

        User userTo = userRepository.findOneByLogin(message.getTo().getLogin()).get();
        User userFrom = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        message.setTo(userTo);
        message.setFrom(userFrom);
        message.setStatus(MessageStatus.NEW);
        message.setDateCreated(ZonedDateTime.now());
        message.setInbox(userTo.getMailBox().getInbox());
        message.setOutbox(userFrom.getMailBox().getOutbox());

        messageRepository.save(message);
    }

    @SubscribeMapping(TOPIC_MAIL_CONFIRM)
    public void confirmMessages(@Payload Message[] messages) {
        List<Message> savedMessages = new ArrayList<>();

        for (Message message : messages) {
            Message saved = messageRepository.findOne(message.getId());
            saved.setStatus(MessageStatus.READ);
            saved.setDateRead(ZonedDateTime.now());
            savedMessages.add(saved);
        }

        messageRepository.save(savedMessages);
    }

    @Override
    public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        String userName = sessionSubscribeEvent.getUser().getName();
        List<String> destinations = StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage())
            .getNativeHeader("destination");

        if (destinations.size() > 0 && destinations.get(0).contains(TOPIC_MAIL_INBOX)) {
            sendInboxMessagesToUser(userName);
        }

        if (destinations.size() > 0 && destinations.get(0).contains(TOPIC_MAIL_OUTBOX)) {
            sendOutboxMessagesToUser(userName);
        }
    }

    public void sendInboxMessagesToUser(String toUser) {
        messagingTemplate.convertAndSendToUser(toUser, TOPIC_MAIL_INBOX, "");
    }

    public void sendOutboxMessagesToUser(String toUser) {
        messagingTemplate.convertAndSendToUser(toUser, TOPIC_MAIL_OUTBOX, "");
    }
}

