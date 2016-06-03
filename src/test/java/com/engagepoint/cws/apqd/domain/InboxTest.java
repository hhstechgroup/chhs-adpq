package com.engagepoint.cws.apqd.domain;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.repository.InboxRepository;
import com.engagepoint.cws.apqd.repository.MailBoxRepository;
import com.engagepoint.cws.apqd.repository.MessageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static com.engagepoint.cws.apqd.APQDTestUtil.assertObjectIdentity;
import static com.engagepoint.cws.apqd.APQDTestUtil.prepareMessage;
import static com.engagepoint.cws.apqd.APQDTestUtil.setMessage;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class InboxTest {
    @Inject
    private InboxRepository inboxRepository;

    @Inject
    private MailBoxRepository mailBoxRepository;

    @Inject
    private MessageRepository messageRepository;

    private Inbox createEntity(String messageSubject, String messageBody) {
        Inbox inbox = new Inbox();
        inbox.setMailBox(mailBoxRepository.saveAndFlush(new MailBox()));
        Message message = prepareMessage(messageRepository, messageSubject, messageBody, null, null);
        return setMessage(inboxRepository, inbox, message);
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

        MailBox testMailBox = testInbox.getMailBox();
        assertThat(testMailBox).isNotNull();
    }

    @Test
    @Transactional
    public void testIdentity() throws Exception {
        Inbox inbox1 = createEntity("message subject 1", "message body 1");
        Inbox inbox2 = createEntity("message subject 2", "message body 2");
        Inbox foundEntity = inboxRepository.findOne(inbox2.getId());

        assertObjectIdentity(inbox1, inbox2, foundEntity, null);
    }
}
