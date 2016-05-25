package com.engagepoint.cws.apqd.web.rest;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.domain.Inbox;
import com.engagepoint.cws.apqd.domain.Message;
import com.engagepoint.cws.apqd.domain.MessageStatus;
import com.engagepoint.cws.apqd.domain.Outbox;
import com.engagepoint.cws.apqd.domain.User;
import com.engagepoint.cws.apqd.repository.InboxRepository;
import com.engagepoint.cws.apqd.repository.MessageRepository;
import com.engagepoint.cws.apqd.repository.OutboxRepository;
import com.engagepoint.cws.apqd.repository.UserRepository;
import com.engagepoint.cws.apqd.repository.search.MessageSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.engagepoint.cws.apqd.domain.enumeration.MessageStatus;

/**
 * Test class for the MessageResource REST controller.
 *
 * @see MessageResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MessageResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_BODY = "AAAAA";
    private static final String UPDATED_BODY = "BBBBB";
    private static final String DEFAULT_SUBJECT = "AAAAA";
    private static final String UPDATED_SUBJECT = "BBBBB";
    private static final String DEFAULT_CASE_NUMBER = "AAAAA";
    private static final String UPDATED_CASE_NUMBER = "BBBBB";

    private static final ZonedDateTime DEFAULT_DATE_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_CREATED_STR = dateTimeFormatter.format(DEFAULT_DATE_CREATED);

    private static final ZonedDateTime DEFAULT_DATE_READ = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_READ = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_READ_STR = dateTimeFormatter.format(DEFAULT_DATE_READ);
    private static final MessageStatus DEFAULT_STATUS = MessageStatus.NEW;
    private static final MessageStatus UPDATED_STATUS = MessageStatus.READ;

    private static String TEST_PASSWORD_HASH = new String(new char[60]).replace("\0", "F");

    @Inject
    private MessageRepository messageRepository;

    @Inject
    private MessageSearchRepository messageSearchRepository;

    @Inject
    private InboxRepository inboxRepository;

    @Inject
    private OutboxRepository outboxRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMessageMockMvc;

    private Message message;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MessageResource messageResource = new MessageResource();
        ReflectionTestUtils.setField(messageResource, "messageSearchRepository", messageSearchRepository);
        ReflectionTestUtils.setField(messageResource, "messageRepository", messageRepository);
        this.restMessageMockMvc = MockMvcBuilders.standaloneSetup(messageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        message = new Message();
        message.setBody(DEFAULT_BODY);
        message.setSubject(DEFAULT_SUBJECT);
        message.setCaseNumber(DEFAULT_CASE_NUMBER);
        message.setDateCreated(DEFAULT_DATE_CREATED);
        message.setDateRead(DEFAULT_DATE_READ);
        message.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void getEntityFields() throws Exception {
        Inbox inbox = new Inbox();
        inboxRepository.saveAndFlush(inbox);

        Outbox outbox = new Outbox();
        outboxRepository.saveAndFlush(outbox);

        Message replyOn = new Message();
        replyOn.setSubject("replyOn message subject");
        replyOn.setBody("replyOn message body");
        messageRepository.saveAndFlush(replyOn);

        User from = new User();
        from.setLogin("user1");
        from.setPassword(TEST_PASSWORD_HASH);
        userRepository.saveAndFlush(from);

        User to = new User();
        to.setLogin("user2");
        to.setPassword(TEST_PASSWORD_HASH);
        userRepository.saveAndFlush(to);

        message.setSubject("message subject");
        message.setBody("message body");
        message.setInbox(inbox);
        message.setOutbox(outbox);
        message.setReplyOn(replyOn);
        message.setFrom(from);
        message.setTo(to);
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
    public void createMessage() throws Exception {
        int databaseSizeBeforeCreate = messageRepository.findAll().size();

        // Create the Message

        restMessageMockMvc.perform(post("/api/messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(message)))
                .andExpect(status().isCreated());

        // Validate the Message in the database
        List<Message> messages = messageRepository.findAll();
        assertThat(messages).hasSize(databaseSizeBeforeCreate + 1);
        Message testMessage = messages.get(messages.size() - 1);
        assertThat(testMessage.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testMessage.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testMessage.getCaseNumber()).isEqualTo(DEFAULT_CASE_NUMBER);
        assertThat(testMessage.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testMessage.getDateRead()).isEqualTo(DEFAULT_DATE_READ);
        assertThat(testMessage.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void checkBodyIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageRepository.findAll().size();
        // set the field null
        message.setBody(null);

        // Create the Message, which fails.

        restMessageMockMvc.perform(post("/api/messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(message)))
                .andExpect(status().isBadRequest());

        List<Message> messages = messageRepository.findAll();
        assertThat(messages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = messageRepository.findAll().size();
        // set the field null
        message.setSubject(null);

        // Create the Message, which fails.

        restMessageMockMvc.perform(post("/api/messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(message)))
                .andExpect(status().isBadRequest());

        List<Message> messages = messageRepository.findAll();
        assertThat(messages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMessages() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messages
        restMessageMockMvc.perform(get("/api/messages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(message.getId().intValue())))
                .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
                .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
                .andExpect(jsonPath("$.[*].caseNumber").value(hasItem(DEFAULT_CASE_NUMBER)))
                .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED_STR)))
                .andExpect(jsonPath("$.[*].dateRead").value(hasItem(DEFAULT_DATE_READ_STR)))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.name())));
    }

    @Test
    @Transactional
    public void getMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get the message
        restMessageMockMvc.perform(get("/api/messages/{id}", message.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(message.getId().intValue()))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.caseNumber").value(DEFAULT_CASE_NUMBER))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED_STR))
            .andExpect(jsonPath("$.dateRead").value(DEFAULT_DATE_READ_STR))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.name()));
    }

    @Test
    @Transactional
    public void getNonExistingMessage() throws Exception {
        // Get the message
        restMessageMockMvc.perform(get("/api/messages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

		int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // Update the message
        message.setBody(UPDATED_BODY);
        message.setSubject(UPDATED_SUBJECT);
        message.setCaseNumber(UPDATED_CASE_NUMBER);
        message.setDateCreated(UPDATED_DATE_CREATED);
        message.setDateRead(UPDATED_DATE_READ);
        message.setStatus(UPDATED_STATUS);

        restMessageMockMvc.perform(put("/api/messages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(message)))
                .andExpect(status().isOk());

        // Validate the Message in the database
        List<Message> messages = messageRepository.findAll();
        assertThat(messages).hasSize(databaseSizeBeforeUpdate);
        Message testMessage = messages.get(messages.size() - 1);
        assertThat(testMessage.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testMessage.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testMessage.getCaseNumber()).isEqualTo(UPDATED_CASE_NUMBER);
        assertThat(testMessage.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testMessage.getDateRead()).isEqualTo(UPDATED_DATE_READ);
        assertThat(testMessage.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

		int databaseSizeBeforeDelete = messageRepository.findAll().size();

        // Get the message
        restMessageMockMvc.perform(delete("/api/messages/{id}", message.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Message> messages = messageRepository.findAll();
        assertThat(messages).hasSize(databaseSizeBeforeDelete - 1);
    }
}
