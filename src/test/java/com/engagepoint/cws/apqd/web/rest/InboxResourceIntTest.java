package com.engagepoint.cws.apqd.web.rest;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.domain.Inbox;
import com.engagepoint.cws.apqd.repository.InboxRepository;
import com.engagepoint.cws.apqd.repository.search.InboxSearchRepository;

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
 * Test class for the InboxResource REST controller.
 *
 * @see InboxResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class InboxResourceIntTest {


    @Inject
    private InboxRepository inboxRepository;

    @Inject
    private InboxSearchRepository inboxSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInboxMockMvc;

    private Inbox inbox;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InboxResource inboxResource = new InboxResource();
        ReflectionTestUtils.setField(inboxResource, "inboxSearchRepository", inboxSearchRepository);
        ReflectionTestUtils.setField(inboxResource, "inboxRepository", inboxRepository);
        this.restInboxMockMvc = MockMvcBuilders.standaloneSetup(inboxResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        inbox = new Inbox();
    }

    @Test
    @Transactional
    public void createInbox() throws Exception {
        int databaseSizeBeforeCreate = inboxRepository.findAll().size();

        // Create the Inbox

        restInboxMockMvc.perform(post("/api/inboxs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(inbox)))
                .andExpect(status().isCreated());

        // Validate the Inbox in the database
        List<Inbox> inboxs = inboxRepository.findAll();
        assertThat(inboxs).hasSize(databaseSizeBeforeCreate + 1);
    }

    @Test
    @Transactional
    public void getAllInboxs() throws Exception {
        // Initialize the database
        inboxRepository.saveAndFlush(inbox);

        // Get all the inboxs
        restInboxMockMvc.perform(get("/api/inboxs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(inbox.getId().intValue())));
    }

    @Test
    @Transactional
    public void getInbox() throws Exception {
        // Initialize the database
        inboxRepository.saveAndFlush(inbox);

        // Get the inbox
        restInboxMockMvc.perform(get("/api/inboxs/{id}", inbox.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(inbox.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingInbox() throws Exception {
        // Get the inbox
        restInboxMockMvc.perform(get("/api/inboxs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInbox() throws Exception {
        // Initialize the database
        inboxRepository.saveAndFlush(inbox);

		int databaseSizeBeforeUpdate = inboxRepository.findAll().size();

        // Update the inbox

        restInboxMockMvc.perform(put("/api/inboxs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(inbox)))
                .andExpect(status().isOk());

        // Validate the Inbox in the database
        List<Inbox> inboxs = inboxRepository.findAll();
        assertThat(inboxs).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteInbox() throws Exception {
        // Initialize the database
        inboxRepository.saveAndFlush(inbox);

		int databaseSizeBeforeDelete = inboxRepository.findAll().size();

        // Get the inbox
        restInboxMockMvc.perform(delete("/api/inboxs/{id}", inbox.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Inbox> inboxs = inboxRepository.findAll();
        assertThat(inboxs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
