package com.engagepoint.cws.apqd.web.rest;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.domain.LookupMaritalStatus;
import com.engagepoint.cws.apqd.repository.LookupMaritalStatusRepository;
import com.engagepoint.cws.apqd.repository.search.LookupMaritalStatusSearchRepository;

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
 * Test class for the LookupMaritalStatusResource REST controller.
 *
 * @see LookupMaritalStatusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class LookupMaritalStatusResourceIntTest {

    private static final String DEFAULT_MARITAL_STATUS_NAME = "AAAAA";
    private static final String UPDATED_MARITAL_STATUS_NAME = "BBBBB";

    @Inject
    private LookupMaritalStatusRepository lookupMaritalStatusRepository;

    @Inject
    private LookupMaritalStatusSearchRepository lookupMaritalStatusSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLookupMaritalStatusMockMvc;

    private LookupMaritalStatus lookupMaritalStatus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LookupMaritalStatusResource lookupMaritalStatusResource = new LookupMaritalStatusResource();
        ReflectionTestUtils.setField(lookupMaritalStatusResource, "lookupMaritalStatusSearchRepository", lookupMaritalStatusSearchRepository);
        ReflectionTestUtils.setField(lookupMaritalStatusResource, "lookupMaritalStatusRepository", lookupMaritalStatusRepository);
        this.restLookupMaritalStatusMockMvc = MockMvcBuilders.standaloneSetup(lookupMaritalStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        lookupMaritalStatus = new LookupMaritalStatus();
        lookupMaritalStatus.setMaritalStatusName(DEFAULT_MARITAL_STATUS_NAME);
    }

    @Test
    @Transactional
    public void createLookupMaritalStatus() throws Exception {
        int databaseSizeBeforeCreate = lookupMaritalStatusRepository.findAll().size();

        // Create the LookupMaritalStatus

        restLookupMaritalStatusMockMvc.perform(post("/api/lookupMaritalStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupMaritalStatus)))
                .andExpect(status().isCreated());

        // Validate the LookupMaritalStatus in the database
        List<LookupMaritalStatus> lookupMaritalStatuss = lookupMaritalStatusRepository.findAll();
        assertThat(lookupMaritalStatuss).hasSize(databaseSizeBeforeCreate + 1);
        LookupMaritalStatus testLookupMaritalStatus = lookupMaritalStatuss.get(lookupMaritalStatuss.size() - 1);
        assertThat(testLookupMaritalStatus.getMaritalStatusName()).isEqualTo(DEFAULT_MARITAL_STATUS_NAME);
    }

    @Test
    @Transactional
    public void checkMaritalStatusNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = lookupMaritalStatusRepository.findAll().size();
        // set the field null
        lookupMaritalStatus.setMaritalStatusName(null);

        // Create the LookupMaritalStatus, which fails.

        restLookupMaritalStatusMockMvc.perform(post("/api/lookupMaritalStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupMaritalStatus)))
                .andExpect(status().isBadRequest());

        List<LookupMaritalStatus> lookupMaritalStatuss = lookupMaritalStatusRepository.findAll();
        assertThat(lookupMaritalStatuss).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLookupMaritalStatuss() throws Exception {
        // Initialize the database
        lookupMaritalStatusRepository.saveAndFlush(lookupMaritalStatus);

        // Get all the lookupMaritalStatuss
        restLookupMaritalStatusMockMvc.perform(get("/api/lookupMaritalStatuss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lookupMaritalStatus.getId().intValue())))
                .andExpect(jsonPath("$.[*].maritalStatusName").value(hasItem(DEFAULT_MARITAL_STATUS_NAME.toString())));
    }

    @Test
    @Transactional
    public void getLookupMaritalStatus() throws Exception {
        // Initialize the database
        lookupMaritalStatusRepository.saveAndFlush(lookupMaritalStatus);

        // Get the lookupMaritalStatus
        restLookupMaritalStatusMockMvc.perform(get("/api/lookupMaritalStatuss/{id}", lookupMaritalStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(lookupMaritalStatus.getId().intValue()))
            .andExpect(jsonPath("$.maritalStatusName").value(DEFAULT_MARITAL_STATUS_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLookupMaritalStatus() throws Exception {
        // Get the lookupMaritalStatus
        restLookupMaritalStatusMockMvc.perform(get("/api/lookupMaritalStatuss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLookupMaritalStatus() throws Exception {
        // Initialize the database
        lookupMaritalStatusRepository.saveAndFlush(lookupMaritalStatus);

		int databaseSizeBeforeUpdate = lookupMaritalStatusRepository.findAll().size();

        // Update the lookupMaritalStatus
        lookupMaritalStatus.setMaritalStatusName(UPDATED_MARITAL_STATUS_NAME);

        restLookupMaritalStatusMockMvc.perform(put("/api/lookupMaritalStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupMaritalStatus)))
                .andExpect(status().isOk());

        // Validate the LookupMaritalStatus in the database
        List<LookupMaritalStatus> lookupMaritalStatuss = lookupMaritalStatusRepository.findAll();
        assertThat(lookupMaritalStatuss).hasSize(databaseSizeBeforeUpdate);
        LookupMaritalStatus testLookupMaritalStatus = lookupMaritalStatuss.get(lookupMaritalStatuss.size() - 1);
        assertThat(testLookupMaritalStatus.getMaritalStatusName()).isEqualTo(UPDATED_MARITAL_STATUS_NAME);
    }

    @Test
    @Transactional
    public void deleteLookupMaritalStatus() throws Exception {
        // Initialize the database
        lookupMaritalStatusRepository.saveAndFlush(lookupMaritalStatus);

		int databaseSizeBeforeDelete = lookupMaritalStatusRepository.findAll().size();

        // Get the lookupMaritalStatus
        restLookupMaritalStatusMockMvc.perform(delete("/api/lookupMaritalStatuss/{id}", lookupMaritalStatus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<LookupMaritalStatus> lookupMaritalStatuss = lookupMaritalStatusRepository.findAll();
        assertThat(lookupMaritalStatuss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
