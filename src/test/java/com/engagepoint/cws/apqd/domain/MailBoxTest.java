package com.engagepoint.cws.apqd.domain;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.repository.DraftRepository;
import com.engagepoint.cws.apqd.repository.InboxRepository;
import com.engagepoint.cws.apqd.repository.MailBoxRepository;
import com.engagepoint.cws.apqd.repository.OutboxRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static com.engagepoint.cws.apqd.APQDTestUtil.assertObjectIdentity;
import static com.engagepoint.cws.apqd.APQDTestUtil.prepareMailBox;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MailBoxTest {
    @Inject
    private MailBoxRepository mailBoxRepository;

    @Inject
    private InboxRepository inboxRepository;

    @Inject
    private OutboxRepository outboxRepository;

    @Inject
    private DraftRepository draftRepository;

    private MailBox createEntity() {
        return prepareMailBox(mailBoxRepository, inboxRepository, outboxRepository, draftRepository);
    }

    @Test
    @Transactional
    public void testEntityFields() throws Exception {
        MailBox mailBox = createEntity();

        MailBox testMailBox = mailBoxRepository.findOne(mailBox.getId());
        assertThat(testMailBox).isNotNull();
        assertThat(testMailBox.getInbox()).isNotNull();
        assertThat(testMailBox.getOutbox()).isNotNull();
        assertThat(testMailBox.getDraft()).isNotNull();
    }

    @Test
    @Transactional
    public void testIdentity() throws Exception {
        MailBox mailBox1 = createEntity();
        MailBox mailBox2 = createEntity();
        MailBox foundEntity = mailBoxRepository.findOne(mailBox2.getId());

        assertObjectIdentity(mailBox1, mailBox2, foundEntity, null);
    }
}
