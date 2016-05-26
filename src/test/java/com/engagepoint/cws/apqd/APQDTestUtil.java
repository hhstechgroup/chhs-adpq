package com.engagepoint.cws.apqd;

import com.engagepoint.cws.apqd.domain.Inbox;
import com.engagepoint.cws.apqd.domain.MailBox;
import com.engagepoint.cws.apqd.domain.Message;
import com.engagepoint.cws.apqd.domain.Outbox;
import com.engagepoint.cws.apqd.domain.User;
import com.engagepoint.cws.apqd.repository.InboxRepository;
import com.engagepoint.cws.apqd.repository.MailBoxRepository;
import com.engagepoint.cws.apqd.repository.MessageRepository;
import com.engagepoint.cws.apqd.repository.OutboxRepository;
import com.engagepoint.cws.apqd.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

public final class APQDTestUtil {
    private static final String TEST_PASSWORD_HASH = new String(new char[60]).replace("\0", "F");

    /**
     * Usage:
     *      TestUtil.setCurrentUser(user);
     *      String login = SecurityUtils.getCurrentUserLogin();
     *      assertThat(login).isEqualTo(user.getLogin());
     *
     * @param user User
     */
    public static void setCurrentUser(User user) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
        SecurityContextHolder.setContext(securityContext);
    }

    public static User prepareUser(UserRepository userRepository, String login) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(TEST_PASSWORD_HASH);
        return userRepository == null ? user : userRepository.saveAndFlush(user);
    }

    public static void addMailBox(UserRepository userRepository, User user, MailBox mailBox) {
        user.setMailBox(mailBox);
        userRepository.saveAndFlush(user);
    }

    public static Inbox prepareInbox(InboxRepository inboxRepository) {
        return inboxRepository.saveAndFlush(new Inbox());
    }

    public static Outbox prepareOutbox(OutboxRepository outboxRepository) {
        return outboxRepository.saveAndFlush(new Outbox());
    }

    public static MailBox prepareMailBox(MailBoxRepository mailBoxRepository, Inbox inbox, Outbox outbox, User user) {
        MailBox mailBox = new MailBox();
        mailBox.setInbox(inbox);
        mailBox.setOutbox(outbox);
        mailBox.setUser(user);
        return mailBoxRepository.saveAndFlush(mailBox);
    }

    public static MailBox prepareMailBox(MailBoxRepository mailBoxRepository, InboxRepository inboxRepository, OutboxRepository outboxRepository) {
        return prepareMailBox(mailBoxRepository, prepareInbox(inboxRepository), prepareOutbox(outboxRepository), null);
    }

    public static Message prepareMessage(MessageRepository messageRepository, String subject, String body, User from, User to) {
        Message message = new Message();
        message.setSubject(subject);
        message.setBody(body);
        message.setFrom(from);
        message.setTo(to);
        return messageRepository.saveAndFlush(message);
    }

    public static Inbox addMessage(InboxRepository inboxRepository, Inbox inbox, Message message) {
        if (inbox.getMessages() == null) {
            inbox.setMessages(new HashSet<>());
        }
        inbox.getMessages().add(message);
        return inboxRepository.saveAndFlush(inbox);
    }

    public static Outbox addMessage(OutboxRepository outboxRepository, Outbox outbox, Message message) {
        if (outbox.getMessages() == null) {
            outbox.setMessages(new HashSet<>());
        }
        outbox.getMessages().add(message);
        return outboxRepository.saveAndFlush(outbox);
    }

    public static <T> void assertIdentity(T entity1, T entity2, T foundEntity, T nullEntity) throws Exception {
        assertThat(entity1.equals(nullEntity)).isFalse(); // null
        assertThat(entity1.equals("")).isFalse(); // other type
        assertThat(entity1.getClass().newInstance().equals(entity1.getClass().newInstance())).isFalse(); // instances with null id
        assertThat(entity1.equals(entity1.getClass().newInstance())).isFalse();
        assertThat(entity1.equals(entity2)).isFalse(); // other entity
        assertThat(foundEntity.equals(entity2)).isTrue();

        assertThat(entity1.hashCode()).isNotNull();
        assertThat(entity1.toString().length()).isGreaterThan(0);
    }
}
