package com.engagepoint.cws.apqd.web.websocket;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.domain.enumeration.MessageStatus;
import com.engagepoint.cws.apqd.domain.Message;
import com.engagepoint.cws.apqd.domain.User;
import com.engagepoint.cws.apqd.repository.InboxRepository;
import com.engagepoint.cws.apqd.repository.MailBoxRepository;
import com.engagepoint.cws.apqd.repository.MessageRepository;
import com.engagepoint.cws.apqd.repository.OutboxRepository;
import com.engagepoint.cws.apqd.repository.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static com.engagepoint.cws.apqd.APQDTestUtil.addMailBox;
import static com.engagepoint.cws.apqd.APQDTestUtil.prepareMailBox;
import static com.engagepoint.cws.apqd.APQDTestUtil.prepareMessage;
import static com.engagepoint.cws.apqd.APQDTestUtil.prepareUser;
import static com.engagepoint.cws.apqd.APQDTestUtil.setCurrentUser;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MailBoxServiceTest {
    private static final String CURRENT_LOGIN = "current1";
    private static final String TO_LOGIN = "userto1";

    @Inject
    private UserRepository userRepository;

    @Inject
    private MessageRepository messageRepository;

    @Inject
    private MailBoxRepository mailBoxRepository;

    @Inject
    private InboxRepository inboxRepository;

    @Inject
    private OutboxRepository outboxRepository;

    private MailBoxService mailBoxService;

    private User currentUser;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mailBoxService = new MailBoxService();

        ReflectionTestUtils.setField(mailBoxService, "userRepository", userRepository);
        ReflectionTestUtils.setField(mailBoxService, "messageRepository", messageRepository);
    }

    @Before
    public void initTest () {
        currentUser = prepareUser(null, CURRENT_LOGIN);
        addMailBox(userRepository, currentUser,
            prepareMailBox(mailBoxRepository, inboxRepository, outboxRepository));
        setCurrentUser(currentUser);
    }

    @Test
    @Transactional
    public void testSendMessage() throws Exception {
        User to = prepareUser(userRepository, TO_LOGIN);
        addMailBox(userRepository, to,
            prepareMailBox(mailBoxRepository, inboxRepository, outboxRepository));

        Message message = prepareMessage(messageRepository, "subject", "body", null, to);
        mailBoxService.sendMessage(message);

        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).isNotNull();
        assertThat(messageList.size()).isGreaterThan(0);
        //
        Message testMessage = messageList.get(0);
        assertThat(testMessage).isNotNull();
        assertThat(testMessage.getSubject()).isEqualTo("subject");
        assertThat(testMessage.getBody()).isEqualTo("body");
        assertThat(testMessage.getStatus()).isEqualTo(MessageStatus.NEW);
        assertThat(testMessage.getDateCreated()).isNotNull();
        //
        assertThat(testMessage.getTo()).isNotNull();
        assertThat(testMessage.getTo().getLogin()).isEqualTo(TO_LOGIN);
        assertThat(testMessage.getFrom()).isNotNull();
        assertThat(testMessage.getFrom().getLogin()).isEqualTo(CURRENT_LOGIN);
        //
        assertThat(testMessage.getInbox()).isNotNull();
        assertThat(testMessage.getInbox()).isEqualTo(to.getMailBox().getInbox());
        //
        assertThat(testMessage.getOutbox()).isNotNull();
        assertThat(testMessage.getOutbox()).isEqualTo(currentUser.getMailBox().getOutbox());
    }
}
