package com.engagepoint.cws.apqd.web.rest;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.MockMailSender;
import com.engagepoint.cws.apqd.config.JHipsterProperties;
import com.engagepoint.cws.apqd.domain.Deleted;
import com.engagepoint.cws.apqd.domain.Message;
import com.engagepoint.cws.apqd.domain.User;
import com.engagepoint.cws.apqd.domain.enumeration.MessageStatus;
import com.engagepoint.cws.apqd.repository.AuthorityRepository;
import com.engagepoint.cws.apqd.repository.DeletedRepository;
import com.engagepoint.cws.apqd.repository.DraftRepository;
import com.engagepoint.cws.apqd.repository.InboxRepository;
import com.engagepoint.cws.apqd.repository.MailBoxRepository;
import com.engagepoint.cws.apqd.repository.MessageRepository;
import com.engagepoint.cws.apqd.repository.OutboxRepository;
import com.engagepoint.cws.apqd.repository.UserRepository;
import com.engagepoint.cws.apqd.repository.search.MessageSearchRepository;
import com.engagepoint.cws.apqd.repository.search.MessageThreadSearchRepository;
import com.engagepoint.cws.apqd.security.AuthoritiesConstants;
import com.engagepoint.cws.apqd.service.MailService;
import com.engagepoint.cws.apqd.web.websocket.MailBoxService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static com.engagepoint.cws.apqd.APQDTestUtil.addUserRole;
import static com.engagepoint.cws.apqd.APQDTestUtil.assertUserEmail;
import static com.engagepoint.cws.apqd.APQDTestUtil.expectHasContact1;
import static com.engagepoint.cws.apqd.APQDTestUtil.expectHasContact2;
import static com.engagepoint.cws.apqd.APQDTestUtil.newUserAnnaBrown;
import static com.engagepoint.cws.apqd.APQDTestUtil.newUserJohnWhite;
import static com.engagepoint.cws.apqd.APQDTestUtil.prepareMailBox;
import static com.engagepoint.cws.apqd.APQDTestUtil.prepareMessage;
import static com.engagepoint.cws.apqd.APQDTestUtil.setCurrentUser;
import static com.engagepoint.cws.apqd.APQDTestUtil.setMailBox;
import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MailResourceIntTest {
    private static final String MSG_SUBJECT = "subject";
    private static final String MSG_SUBJECT_UPDATED = "subject updated";
    private static final String MSG_BODY = "body";
    private static final String MSG_BODY_UPDATED = "body updated";

    enum MAIL_FILTER {
        NONE,
        LOGIN,
        BODY
    }

    @Inject
    private InboxRepository inboxRepository;

    @Inject
    private OutboxRepository outboxRepository;

    @Inject
    private DraftRepository draftRepository;

    @Inject
    private DeletedRepository deletedRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private MessageRepository messageRepository;

    @Inject
    private MailBoxService mailBoxService;

    @Inject
    private MailBoxRepository mailBoxRepository;

    @Inject
    private MessageSearchRepository messageSearchRepository;

    @Inject
    private MessageThreadSearchRepository messageThreadSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMailSender mockMailSender;

    private MockMvc restResourceMockMvc;

    private User fromUser;
    private User toUser;

    @Inject
    private JHipsterProperties jHipsterProperties;

    @Inject
    private MessageSource messageSource;

    @Inject
    private SpringTemplateEngine templateEngine;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);

        mockMailSender = new MockMailSender();

        MailService mailService = new MailService();
        ReflectionTestUtils.setField(mailService, "jHipsterProperties", jHipsterProperties);
        ReflectionTestUtils.setField(mailService, "messageSource", messageSource);
        ReflectionTestUtils.setField(mailService, "templateEngine", templateEngine);
        ReflectionTestUtils.setField(mailService, "javaMailSender", mockMailSender);

        MailResource mailResource = new MailResource();
        ReflectionTestUtils.setField(mailResource, "userRepository", userRepository);
        ReflectionTestUtils.setField(mailResource, "messageRepository", messageRepository);
        ReflectionTestUtils.setField(mailResource, "deletedRepository", deletedRepository);
        ReflectionTestUtils.setField(mailResource, "mailBoxService", mailBoxService);
        ReflectionTestUtils.setField(mailResource, "mailService", mailService);
        ReflectionTestUtils.setField(mailResource, "mailBoxRepository", mailBoxRepository);
        ReflectionTestUtils.setField(mailResource, "messageSearchRepository", messageSearchRepository);
        ReflectionTestUtils.setField(mailResource, "messageThreadSearchRepository", messageThreadSearchRepository);

        ContactResource contactResource = new ContactResource();
        ReflectionTestUtils.setField(contactResource, "userRepository", userRepository);
        ReflectionTestUtils.setField(contactResource, "authorityRepository", authorityRepository);

        MessageResource messageResource = new MessageResource();
        ReflectionTestUtils.setField(messageResource, "messageRepository", messageRepository);
        ReflectionTestUtils.setField(messageResource, "messageSearchRepository", messageSearchRepository);

        this.restResourceMockMvc = MockMvcBuilders.standaloneSetup(mailResource, contactResource, messageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void before() {
        messageRepository.deleteAll();
        messageSearchRepository.deleteAll();
        messageThreadSearchRepository.deleteAll();
    }

    private Message prepareData() {
        // create Users and Mailboxes

        fromUser = newUserAnnaBrown(passwordEncoder, authorityRepository);
        addUserRole(authorityRepository, fromUser, AuthoritiesConstants.CASE_WORKER);
        setMailBox(userRepository, fromUser,
            prepareMailBox(mailBoxRepository, inboxRepository, outboxRepository, draftRepository));

        setCurrentUser(fromUser);

        toUser = newUserJohnWhite(passwordEncoder, authorityRepository);
        addUserRole(authorityRepository, toUser, AuthoritiesConstants.PARENT);
        setMailBox(userRepository, toUser,
            prepareMailBox(mailBoxRepository, inboxRepository, outboxRepository, draftRepository));

        // create new Message with no id

        return prepareMessage(null, MSG_SUBJECT, MSG_BODY, fromUser, toUser);
    }

    private void assertCreateMessage(Message newMessage) throws Exception {
        assertThat(newMessage.getId()).isNull();

        restResourceMockMvc.perform(
            put("/api/mails/draft")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(
                    newMessage
                )))
            .andExpect(status().isCreated());
    }

    private void assertGetMessages(Message testMessage, EMailDirectory eMailDirectory, MAIL_FILTER mailFilter) throws Exception {
        assertThat(testMessage.getId()).isNotNull();

        String searchWord = "-1";
        if (mailFilter == MAIL_FILTER.LOGIN) {
            searchWord = "BY_LOGIN_" + (eMailDirectory == EMailDirectory.INBOX
                ? testMessage.getFrom().getLogin() : testMessage.getTo().getLogin());
        } else if (mailFilter == MAIL_FILTER.BODY) {
            searchWord = testMessage.getBody().split(" ")[0];
        }

        restResourceMockMvc.perform(
            get(String.format("/api/mails/%s/%s?sort=id,desc", eMailDirectory, searchWord)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(testMessage.getSubject())))
            .andExpect(jsonPath("$.[*].body").value(hasItem(testMessage.getBody())));
    }

    private void assertUpdateMessage(Message updatedMessage) throws Exception {
        assertThat(updatedMessage.getId()).isNotNull();

        restResourceMockMvc.perform(
            put("/api/mails/draft")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(
                    updatedMessage
                )))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(updatedMessage.getId().intValue()))
            .andExpect(jsonPath("$.subject").value(updatedMessage.getSubject()))
            .andExpect(jsonPath("$.body").value(updatedMessage.getBody()));
    }

    private void assertSendMessage(Message message) throws Exception {
        assertThat(message.getId()).isNotNull();

        restResourceMockMvc.perform(
            post("/api/mails/draft")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(
                    message
                )))
            .andExpect(status().isOk());

        assertUserEmail(jHipsterProperties, messageSource, templateEngine, mockMailSender,
            message.getTo(), "email.newMessage.alert.title", "newMessageAlertEmail");
    }

    private void assertConfirmReading(Message message) throws Exception {
        assertThat(message.getId()).isNotNull();

        restResourceMockMvc.perform(
            post("/api/mails/confirm")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(
                    message
                )))
            .andExpect(status().isOk());
    }

    private void assertMessageThread(Message message) throws Exception {
        assertThat(message.getId()).isNotNull();

        restResourceMockMvc.perform(
            get("/api/mails/thread/" + message.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.thread").exists())
            .andExpect(jsonPath("$.thread[*].id").value(hasItem(message.getId().intValue())))
            .andExpect(jsonPath("$.thread[*].subject").value(hasItem(message.getSubject())))
            .andExpect(jsonPath("$.thread[*].body").value(hasItem(message.getBody())));
    }

    private void assertGetContactsForMailTo(User expectedContact) throws Exception {
        ResultActions resultActions = restResourceMockMvc.perform(
            get("/api/contacts"))
            .andExpect(status().isOk());

        expectHasContact1(resultActions, expectedContact);
    }

    private void assertGetContactsList(User expectedContact) throws Exception {
        ResultActions resultActions = restResourceMockMvc.perform(
            post("/api/contacts"))
            .andExpect(status().isOk());

        expectHasContact2(resultActions, expectedContact);
    }

    private void assertDeleteMessages(Message[] messages) throws Exception {
        restResourceMockMvc.perform(
            post("/api/mails/delete")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(
                    messages
                )))
            .andExpect(status().isOk());
    }

    private void assertRestoreMessages(Message[] messages) throws Exception {
        restResourceMockMvc.perform(
            post("/api/mails/restore")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(
                    messages
                )))
            .andExpect(status().isOk());
    }

    private void assertSearchMessages(Message message) throws Exception {
        restResourceMockMvc.perform(
            get("/api/_search/messages/+id:" + message.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(message.getId().intValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(message.getSubject())))
            .andExpect(jsonPath("$.[*].body").value(hasItem(message.getBody())));
    }

    @Test
    @Transactional
    public void testCreateGetUpdateSendRead() throws Exception {

        // test create

        assertCreateMessage(prepareData());

        Message testMessage = messageRepository.findAll().iterator().next();
        assertThat(testMessage.getStatus()).isEqualTo(MessageStatus.DRAFT);
        assertThat(testMessage.getDateCreated()).isNotNull();
        assertThat(testMessage.getDateUpdated()).isNull();
        assertThat(testMessage.getUnreadMessagesCount()).isEqualTo(0);

        // test get from fromUser drafts folder

        assertGetMessages(testMessage, EMailDirectory.DRAFTS, MAIL_FILTER.NONE);

        // test update

        testMessage.setSubject(MSG_SUBJECT_UPDATED);
        testMessage.setBody(MSG_BODY_UPDATED);

        assertUpdateMessage(testMessage);

        testMessage = messageRepository.findOne(testMessage.getId());
        assertThat(testMessage.getStatus()).isEqualTo(MessageStatus.DRAFT);
        assertThat(testMessage.getDateCreated()).isNotNull();
        assertThat(testMessage.getDateUpdated()).isNull();
        assertThat(testMessage.getUnreadMessagesCount()).isEqualTo(0);

        // test send

        assertSendMessage(testMessage);

        testMessage = messageRepository.findOne(testMessage.getId());
        assertThat(testMessage.getStatus()).isEqualTo(MessageStatus.UNREAD);
        assertThat(testMessage.getDateCreated()).isNotNull();
        assertThat(testMessage.getDateUpdated()).isNotNull();
        assertThat(testMessage.getUnreadMessagesCountFrom()).isEqualTo(0);
        assertThat(testMessage.getUnreadMessagesCountTo()).isEqualTo(1);

        // test get from fromUser sent folder

        assertGetMessages(testMessage, EMailDirectory.SENT, MAIL_FILTER.NONE);
        assertGetMessages(testMessage, EMailDirectory.SENT, MAIL_FILTER.LOGIN);
        assertGetMessages(testMessage, EMailDirectory.SENT, MAIL_FILTER.BODY);

        testMessage = messageRepository.findOne(testMessage.getId());
        assertThat(testMessage.getStatus()).isEqualTo(MessageStatus.UNREAD);

        // test get from toUser inbox folder

        setCurrentUser(toUser);

        assertGetMessages(testMessage, EMailDirectory.INBOX, MAIL_FILTER.NONE);
        assertGetMessages(testMessage, EMailDirectory.INBOX, MAIL_FILTER.LOGIN);
        assertGetMessages(testMessage, EMailDirectory.INBOX, MAIL_FILTER.BODY);

        // test confirmReading

        assertConfirmReading(testMessage);

        testMessage = messageRepository.findOne(testMessage.getId());
        assertThat(testMessage.getStatus()).isEqualTo(MessageStatus.READ);

        // test message thread

        assertMessageThread(testMessage);

        // test search Messages

        assertSearchMessages(testMessage);

        // test delete messages

        Message[] messages = new Message[]{ testMessage };
        assertDeleteMessages(messages);

        Deleted deleted = deletedRepository.findOneByMessageAndDeletedBy(testMessage, toUser);
        assertThat(deleted).isNotNull();
        assertThat(deleted.getDeletedDate()).isNotNull();
        assertThat(deleted.getMessage()).isEqualTo(testMessage);
        assertThat(deleted.getDeletedBy()).isEqualTo(toUser);

        // test restore messages

        assertRestoreMessages(messages);

        deleted = deletedRepository.findOneByMessageAndDeletedBy(testMessage, toUser);
        assertThat(deleted).isNull();

        // test ContactResource.getContactsForMailTo and getContactsList

        setCurrentUser(fromUser);
        assertGetContactsForMailTo(toUser);
        assertGetContactsList(toUser);

        setCurrentUser(toUser);
        assertGetContactsForMailTo(fromUser);
        assertGetContactsList(fromUser);
    }
}
