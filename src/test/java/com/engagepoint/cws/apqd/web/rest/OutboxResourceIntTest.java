package com.engagepoint.cws.apqd.web.rest;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.domain.Outbox;
import com.engagepoint.cws.apqd.repository.OutboxRepository;
import com.engagepoint.cws.apqd.repository.search.OutboxSearchRepository;

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
 * Test class for the OutboxResource REST controller.
 *
 * @see OutboxResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OutboxResourceIntTest {


    @Inject
    private OutboxRepository outboxRepository;

    @Inject
    private OutboxSearchRepository outboxSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOutboxMockMvc;

    private Outbox outbox;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OutboxResource outboxResource = new OutboxResource();
        ReflectionTestUtils.setField(outboxResource, "outboxSearchRepository", outboxSearchRepository);
        ReflectionTestUtils.setField(outboxResource, "outboxRepository", outboxRepository);
        this.restOutboxMockMvc = MockMvcBuilders.standaloneSetup(outboxResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        outbox = new Outbox();
    }

    @Test
    @Transactional
    public void createOutbox() throws Exception {
        int databaseSizeBeforeCreate = outboxRepository.findAll().size();

        // Create the Outbox

        restOutboxMockMvc.perform(post("/api/outboxs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(outbox)))
                .andExpect(status().isCreated());

        // Validate the Outbox in the database
        List<Outbox> outboxs = outboxRepository.findAll();
        assertThat(outboxs).hasSize(databaseSizeBeforeCreate + 1);
    }

    @Test
    @Transactional
    public void getAllOutboxs() throws Exception {
        // Initialize the database
        outboxRepository.saveAndFlush(outbox);

        // Get all the outboxs
        restOutboxMockMvc.perform(get("/api/outboxs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(outbox.getId().intValue())));
    }

    @Test
    @Transactional
    public void getOutbox() throws Exception {
        // Initialize the database
        outboxRepository.saveAndFlush(outbox);

        // Get the outbox
        restOutboxMockMvc.perform(get("/api/outboxs/{id}", outbox.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(outbox.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingOutbox() throws Exception {
        // Get the outbox
        restOutboxMockMvc.perform(get("/api/outboxs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOutbox() throws Exception {
        // Initialize the database
        outboxRepository.saveAndFlush(outbox);

		int databaseSizeBeforeUpdate = outboxRepository.findAll().size();

        // Update the outbox

        restOutboxMockMvc.perform(put("/api/outboxs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(outbox)))
                .andExpect(status().isOk());

        // Validate the Outbox in the database
        List<Outbox> outboxs = outboxRepository.findAll();
        assertThat(outboxs).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOutbox() throws Exception {
        // Initialize the database
        outboxRepository.saveAndFlush(outbox);

		int databaseSizeBeforeDelete = outboxRepository.findAll().size();

        // Get the outbox
        restOutboxMockMvc.perform(delete("/api/outboxs/{id}", outbox.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Outbox> outboxs = outboxRepository.findAll();
        assertThat(outboxs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
