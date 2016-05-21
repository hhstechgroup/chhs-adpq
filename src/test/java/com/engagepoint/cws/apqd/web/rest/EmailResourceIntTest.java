package com.engagepoint.cws.apqd.web.rest;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.domain.Email;
import com.engagepoint.cws.apqd.repository.EmailRepository;
import com.engagepoint.cws.apqd.repository.search.EmailSearchRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EmailResource REST controller.
 *
 * @see EmailResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class EmailResourceIntTest {

    private static final String DEFAULT_EMAIL_TEXT = "AAAAA";
    private static final String UPDATED_EMAIL_TEXT = "BBBBB";

    private static final Boolean DEFAULT_PREFERRED = false;
    private static final Boolean UPDATED_PREFERRED = true;

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private EmailRepository emailRepository;

    @Inject
    private EmailSearchRepository emailSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEmailMockMvc;

    private Email email;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EmailResource emailResource = new EmailResource();
        ReflectionTestUtils.setField(emailResource, "emailSearchRepository", emailSearchRepository);
        ReflectionTestUtils.setField(emailResource, "emailRepository", emailRepository);
        this.restEmailMockMvc = MockMvcBuilders.standaloneSetup(emailResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        email = new Email();
        email.setEmailText(DEFAULT_EMAIL_TEXT);
        email.setPreferred(DEFAULT_PREFERRED);
        email.setStartDate(DEFAULT_START_DATE);
        email.setEndDate(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createEmail() throws Exception {
        int databaseSizeBeforeCreate = emailRepository.findAll().size();

        // Create the Email

        restEmailMockMvc.perform(post("/api/emails")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(email)))
                .andExpect(status().isCreated());

        // Validate the Email in the database
        List<Email> emails = emailRepository.findAll();
        assertThat(emails).hasSize(databaseSizeBeforeCreate + 1);
        Email testEmail = emails.get(emails.size() - 1);
        assertThat(testEmail.getEmailText()).isEqualTo(DEFAULT_EMAIL_TEXT);
        assertThat(testEmail.getPreferred()).isEqualTo(DEFAULT_PREFERRED);
        assertThat(testEmail.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testEmail.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void checkEmailTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = emailRepository.findAll().size();
        // set the field null
        email.setEmailText(null);

        // Create the Email, which fails.

        restEmailMockMvc.perform(post("/api/emails")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(email)))
                .andExpect(status().isBadRequest());

        List<Email> emails = emailRepository.findAll();
        assertThat(emails).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEmails() throws Exception {
        // Initialize the database
        emailRepository.saveAndFlush(email);

        // Get all the emails
        restEmailMockMvc.perform(get("/api/emails?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(email.getId().intValue())))
                .andExpect(jsonPath("$.[*].emailText").value(hasItem(DEFAULT_EMAIL_TEXT.toString())))
                .andExpect(jsonPath("$.[*].preferred").value(hasItem(DEFAULT_PREFERRED.booleanValue())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void getEmail() throws Exception {
        // Initialize the database
        emailRepository.saveAndFlush(email);

        // Get the email
        restEmailMockMvc.perform(get("/api/emails/{id}", email.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(email.getId().intValue()))
            .andExpect(jsonPath("$.emailText").value(DEFAULT_EMAIL_TEXT.toString()))
            .andExpect(jsonPath("$.preferred").value(DEFAULT_PREFERRED.booleanValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEmail() throws Exception {
        // Get the email
        restEmailMockMvc.perform(get("/api/emails/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEmail() throws Exception {
        // Initialize the database
        emailRepository.saveAndFlush(email);

		int databaseSizeBeforeUpdate = emailRepository.findAll().size();

        // Update the email
        email.setEmailText(UPDATED_EMAIL_TEXT);
        email.setPreferred(UPDATED_PREFERRED);
        email.setStartDate(UPDATED_START_DATE);
        email.setEndDate(UPDATED_END_DATE);

        restEmailMockMvc.perform(put("/api/emails")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(email)))
                .andExpect(status().isOk());

        // Validate the Email in the database
        List<Email> emails = emailRepository.findAll();
        assertThat(emails).hasSize(databaseSizeBeforeUpdate);
        Email testEmail = emails.get(emails.size() - 1);
        assertThat(testEmail.getEmailText()).isEqualTo(UPDATED_EMAIL_TEXT);
        assertThat(testEmail.getPreferred()).isEqualTo(UPDATED_PREFERRED);
        assertThat(testEmail.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testEmail.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void deleteEmail() throws Exception {
        // Initialize the database
        emailRepository.saveAndFlush(email);

		int databaseSizeBeforeDelete = emailRepository.findAll().size();

        // Get the email
        restEmailMockMvc.perform(delete("/api/emails/{id}", email.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Email> emails = emailRepository.findAll();
        assertThat(emails).hasSize(databaseSizeBeforeDelete - 1);
    }
}
