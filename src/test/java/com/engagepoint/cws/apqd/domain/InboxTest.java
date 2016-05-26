package com.engagepoint.cws.apqd.domain;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.repository.InboxRepository;
import com.engagepoint.cws.apqd.repository.MessageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static com.engagepoint.cws.apqd.APQDTestUtil.addMessage;
import static com.engagepoint.cws.apqd.APQDTestUtil.prepareMessage;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class InboxTest {
    @Inject
    private InboxRepository inboxRepository;

    @Inject
    private MessageRepository messageRepository;

    private Inbox createEntity(String messageSubject, String messageBody) {
        Inbox inbox = new Inbox();
        Message message = prepareMessage(messageRepository, messageSubject, messageBody, null, null);
        return addMessage(inboxRepository, inbox, message);
    }

    @Test
    @Transactional
    public void testEntityFields() throws Exception {
        Inbox inbox = createEntity("message subject", "message body");
        Message message = inbox.getMessages().iterator().next();

        Inbox testInbox = inboxRepository.findOne(inbox.getId());
        assertThat(testInbox).isNotNull();
        assertThat(testInbox.getMessages()).isNotNull();
        assertThat(testInbox.getMessages().size()).isGreaterThan(0);

        Message testMessage = testInbox.getMessages().iterator().next();
        assertThat(testMessage.getSubject()).isEqualTo(message.getSubject());
        assertThat(testMessage.getBody()).isEqualTo(message.getBody());
    }

    @Test
    @Transactional
    public void testIdentity() throws Exception {
        Inbox inbox1 = createEntity("message subject 1", "message body 1");
        Inbox inbox2 = createEntity("message subject 2", "message body 2");

        assertThat(new Inbox().equals(new Inbox())).isFalse();
        assertThat(inbox1.equals(new Inbox())).isFalse();
        assertThat(inbox1.equals(inbox2)).isFalse();
        assertThat(inboxRepository.findOne(inbox2.getId()).equals(inbox2)).isTrue();

        assertThat(inbox1.hashCode()).isNotNull();
        assertThat(inbox1.toString().length()).isGreaterThan(0);
    }
}
