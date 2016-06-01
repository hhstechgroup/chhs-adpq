package com.engagepoint.cws.apqd.domain;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.repository.DeletedRepository;
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
public class DeletedTest {
    @Inject
    private DeletedRepository deletedRepository;

    @Inject
    private MailBoxRepository mailBoxRepository;

    @Inject
    private MessageRepository messageRepository;

    private Deleted createEntity(String messageSubject, String messageBody) {
        Deleted deleted = new Deleted();
        deleted.setMailBox(mailBoxRepository.saveAndFlush(new MailBox()));
        Message message = prepareMessage(messageRepository, messageSubject, messageBody, null, null);
        return setMessage(deletedRepository, deleted, message);
    }

    @Test
    @Transactional
    public void testEntityFields() throws Exception {
        Deleted deleted = createEntity("message subject", "message body");
        Message message = deleted.getMessages().iterator().next();

        Deleted testDeleted = deletedRepository.findOne(deleted.getId());
        assertThat(testDeleted).isNotNull();
        assertThat(testDeleted.getMessages()).isNotNull();
        assertThat(testDeleted.getMessages().size()).isGreaterThan(0);

        Message testMessage = testDeleted.getMessages().iterator().next();
        assertThat(testMessage.getSubject()).isEqualTo(message.getSubject());
        assertThat(testMessage.getBody()).isEqualTo(message.getBody());

        MailBox testMailBox = testDeleted.getMailBox();
        assertThat(testMailBox).isNotNull();
    }

    @Test
    @Transactional
    public void testIdentity() throws Exception {
        Deleted deleted1 = createEntity("message subject 1", "message body 1");
        Deleted deleted2 = createEntity("message subject 2", "message body 2");
        Deleted foundEntity = deletedRepository.findOne(deleted2.getId());

        assertObjectIdentity(deleted1, deleted2, foundEntity, null);
    }
}
