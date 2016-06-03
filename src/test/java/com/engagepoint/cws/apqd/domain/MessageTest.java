package com.engagepoint.cws.apqd.domain;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.repository.InboxRepository;
import com.engagepoint.cws.apqd.repository.MessageRepository;
import com.engagepoint.cws.apqd.repository.OutboxRepository;
import com.engagepoint.cws.apqd.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static com.engagepoint.cws.apqd.APQDTestUtil.assertObjectIdentity;
import static com.engagepoint.cws.apqd.APQDTestUtil.prepareInbox;
import static com.engagepoint.cws.apqd.APQDTestUtil.prepareMessage;
import static com.engagepoint.cws.apqd.APQDTestUtil.prepareOutbox;
import static com.engagepoint.cws.apqd.APQDTestUtil.prepareUser;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MessageTest {
    @Inject
    private MessageRepository messageRepository;

    @Inject
    private InboxRepository inboxRepository;

    @Inject
    private OutboxRepository outboxRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    private Message createEntity(String messageSubject, String messageBody) {
        return prepareMessage(messageRepository, messageSubject, messageBody, null, null);
    }

    @Test
    @Transactional
    public void testEntityFields() throws Exception {
        Message replyOn = createEntity("replyOn message subject", "replyOn message body");

        User from = prepareUser(userRepository, passwordEncoder, "user1");
        User to = prepareUser(userRepository, passwordEncoder, "user2");

        Message message = prepareMessage(messageRepository, "message subject", "message body", from, to);
        message.setInbox(prepareInbox(inboxRepository));
        message.setOutbox(prepareOutbox(outboxRepository));
        message.setReplyOn(replyOn);
        messageRepository.saveAndFlush(message);

        Message testMessage = messageRepository.findOne(message.getId());
        assertThat(testMessage).isNotNull();
        assertThat(testMessage.getInbox()).isNotNull();
        assertThat(testMessage.getOutbox()).isNotNull();
        assertThat(testMessage.getReplyOn()).isNotNull();
        assertThat(testMessage.getFrom()).isNotNull();
        assertThat(testMessage.getTo()).isNotNull();
    }

    @Test
    @Transactional
    public void testIdentity() throws Exception {
        Message message1 = createEntity("replyOn message subject 1", "replyOn message body 1");
        Message message2 = createEntity("replyOn message subject 2", "replyOn message body 2");
        Message foundEntity = messageRepository.findOne(message2.getId());

        assertObjectIdentity(message1, message2, foundEntity, null);
    }
}
