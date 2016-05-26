package com.engagepoint.cws.apqd.web.rest;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.domain.MailBox;
import com.engagepoint.cws.apqd.repository.MailBoxRepository;
import com.engagepoint.cws.apqd.repository.search.MailBoxSearchRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the MailBoxResource REST controller.
 *
 * @see MailBoxResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MailBoxResourceIntTest {


    @Inject
    private MailBoxRepository mailBoxRepository;

    @Inject
    private MailBoxSearchRepository mailBoxSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMailBoxMockMvc;

    private MailBox mailBox;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MailBoxResource mailBoxResource = new MailBoxResource();
        ReflectionTestUtils.setField(mailBoxResource, "mailBoxSearchRepository", mailBoxSearchRepository);
        ReflectionTestUtils.setField(mailBoxResource, "mailBoxRepository", mailBoxRepository);
        this.restMailBoxMockMvc = MockMvcBuilders.standaloneSetup(mailBoxResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        mailBox = new MailBox();
    }

    @Test
    @Transactional
    public void createMailBox() throws Exception {
        int databaseSizeBeforeCreate = mailBoxRepository.findAll().size();

        // Create the MailBox

        restMailBoxMockMvc.perform(post("/api/mailBoxs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mailBox)))
                .andExpect(status().isCreated());

        // Validate the MailBox in the database
        List<MailBox> mailBoxs = mailBoxRepository.findAll();
        assertThat(mailBoxs).hasSize(databaseSizeBeforeCreate + 1);
        MailBox testMailBox = mailBoxs.get(mailBoxs.size() - 1);
    }

    @Test
    @Transactional
    public void getAllMailBoxs() throws Exception {
        // Initialize the database
        mailBoxRepository.saveAndFlush(mailBox);

        // Get all the mailBoxs
        restMailBoxMockMvc.perform(get("/api/mailBoxs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(mailBox.getId().intValue())));
    }

    @Test
    @Transactional
    public void getMailBox() throws Exception {
        // Initialize the database
        mailBoxRepository.saveAndFlush(mailBox);

        // Get the mailBox
        restMailBoxMockMvc.perform(get("/api/mailBoxs/{id}", mailBox.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(mailBox.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingMailBox() throws Exception {
        // Get the mailBox
        restMailBoxMockMvc.perform(get("/api/mailBoxs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMailBox() throws Exception {
        // Initialize the database
        mailBoxRepository.saveAndFlush(mailBox);

		int databaseSizeBeforeUpdate = mailBoxRepository.findAll().size();

        // Update the mailBox

        restMailBoxMockMvc.perform(put("/api/mailBoxs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mailBox)))
                .andExpect(status().isOk());

        // Validate the MailBox in the database
        List<MailBox> mailBoxs = mailBoxRepository.findAll();
        assertThat(mailBoxs).hasSize(databaseSizeBeforeUpdate);
        MailBox testMailBox = mailBoxs.get(mailBoxs.size() - 1);
    }

    @Test
    @Transactional
    public void deleteMailBox() throws Exception {
        // Initialize the database
        mailBoxRepository.saveAndFlush(mailBox);

		int databaseSizeBeforeDelete = mailBoxRepository.findAll().size();

        // Get the mailBox
        restMailBoxMockMvc.perform(delete("/api/mailBoxs/{id}", mailBox.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MailBox> mailBoxs = mailBoxRepository.findAll();
        assertThat(mailBoxs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
