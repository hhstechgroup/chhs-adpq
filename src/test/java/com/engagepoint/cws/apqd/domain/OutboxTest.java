package com.engagepoint.cws.apqd.domain;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.repository.MessageRepository;
import com.engagepoint.cws.apqd.repository.OutboxRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class OutboxTest {
    @Inject
    private OutboxRepository outboxRepository;

    @Inject
    private MessageRepository messageRepository;

    private Outbox createEntity(String messageSubject, String messageBody) {
        Outbox inbox = new Outbox();
        Message message = prepareMessage(messageRepository, messageSubject, messageBody, null, null);
        return addMessage(outboxRepository, inbox, message);
    }

    @Test
    @Transactional
    public void testEntityFields() throws Exception {
        Outbox outbox = createEntity("message subject", "message body");
        Message message = outbox.getMessages().iterator().next();

        Outbox testOutbox = outboxRepository.findOne(outbox.getId());
        assertThat(testOutbox).isNotNull();
        assertThat(testOutbox.getMessages()).isNotNull();
        assertThat(testOutbox.getMessages().size()).isGreaterThan(0);

        Message testMessage = testOutbox.getMessages().iterator().next();
        assertThat(testMessage.getSubject()).isEqualTo(message.getSubject());
        assertThat(testMessage.getBody()).isEqualTo(message.getBody());
    }

    @Test
    @Transactional
    public void testIdentity() throws Exception {
        Outbox outbox1 = createEntity("message subject 1", "message body 1");
        Outbox outbox2 = createEntity("message subject 2", "message body 2");

        assertThat(new Outbox().equals(new Outbox())).isFalse();
        assertThat(outbox1.equals(new Outbox())).isFalse();
        assertThat(outbox1.equals(outbox2)).isFalse();
        assertThat(outboxRepository.findOne(outbox2.getId()).equals(outbox2)).isTrue();

        assertThat(outbox1.hashCode()).isNotNull();
        assertThat(outbox1.toString().length()).isGreaterThan(0);
    }
}
