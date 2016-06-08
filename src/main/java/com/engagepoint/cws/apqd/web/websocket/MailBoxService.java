package com.engagepoint.cws.apqd.web.websocket;

import com.engagepoint.cws.apqd.domain.User;
import com.engagepoint.cws.apqd.domain.enumeration.MessageStatus;
import com.engagepoint.cws.apqd.repository.MessageRepository;
import com.engagepoint.cws.apqd.repository.UserRepository;
import com.engagepoint.cws.apqd.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

@Controller
public class MailBoxService {

    public static final String TOPIC_MAIL_INBOX = "/topic/mail/inbox";
    public static final String TOPIC_MAIL_DRAFTS = "/topic/mail/drafts";
    public static final String TOPIC_MAIL_DELETED = "/topic/mail/deleted";

    @Inject
    private UserRepository userRepository;

    @Inject
    private MessageRepository messageRepository;

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @SubscribeMapping(TOPIC_MAIL_INBOX)
    public void receiveUnreadCounts() {
        User userFrom = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        notifyClientAboutUnreadInboxCount(userFrom);
        notifyClientAboutDeletedCount();
        notifyClientAboutDraftsCount();
    }

    public void notifyClientAboutUnreadInboxCount(User userTo) {
        Long unreadInboxCount = messageRepository.countByInbox(userTo, MessageStatus.UNREAD);
        messagingTemplate.convertAndSendToUser(userTo.getLogin(), TOPIC_MAIL_INBOX, unreadInboxCount);
    }

    public void notifyClientAboutDraftsCount() {
        User userFrom = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        Long draftsCount = messageRepository.countByDrafts(userFrom);
        messagingTemplate.convertAndSendToUser(userFrom.getLogin(), TOPIC_MAIL_DRAFTS, draftsCount);
    }

    public void notifyClientAboutDeletedCount() {
        User userFrom = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        Long draftsCount = messageRepository.countByDeleted(userFrom);
        messagingTemplate.convertAndSendToUser(userFrom.getLogin(), TOPIC_MAIL_DELETED, draftsCount);
    }
}

