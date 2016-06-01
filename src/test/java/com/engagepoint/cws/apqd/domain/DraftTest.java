package com.engagepoint.cws.apqd.domain;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.repository.DraftRepository;
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
public class DraftTest {
    @Inject
    private DraftRepository draftRepository;

    @Inject
    private MailBoxRepository mailBoxRepository;

    @Inject
    private MessageRepository messageRepository;

    private Draft createEntity(String messageSubject, String messageBody) {
        Draft draft = new Draft();
        draft.setMailBox(mailBoxRepository.saveAndFlush(new MailBox()));
        Message message = prepareMessage(messageRepository, messageSubject, messageBody, null, null);
        return setMessage(draftRepository, draft, message);
    }

    @Test
    @Transactional
    public void testEntityFields() throws Exception {
        Draft draft = createEntity("message subject", "message body");
        Message message = draft.getMessages().iterator().next();

        Draft testDraft = draftRepository.findOne(draft.getId());
        assertThat(testDraft).isNotNull();
        assertThat(testDraft.getMessages()).isNotNull();
        assertThat(testDraft.getMessages().size()).isGreaterThan(0);

        Message testMessage = testDraft.getMessages().iterator().next();
        assertThat(testMessage.getSubject()).isEqualTo(message.getSubject());
        assertThat(testMessage.getBody()).isEqualTo(message.getBody());

        MailBox testMailBox = testDraft.getMailBox();
        assertThat(testMailBox).isNotNull();
    }

    @Test
    @Transactional
    public void testIdentity() throws Exception {
        Draft draft1 = createEntity("message subject 1", "message body 1");
        Draft draft2 = createEntity("message subject 2", "message body 2");
        Draft foundEntity = draftRepository.findOne(draft2.getId());

        assertObjectIdentity(draft1, draft2, foundEntity, null);
    }
}
