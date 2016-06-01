package com.engagepoint.cws.apqd.web.rest;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.domain.LookupState;
import com.engagepoint.cws.apqd.repository.LookupStateRepository;
import com.engagepoint.cws.apqd.repository.search.LookupStateSearchRepository;

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
 * Test class for the LookupStateResource REST controller.
 *
 * @see LookupStateResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class LookupStateResourceIntTest {

    private static final String DEFAULT_STATE_CODE = "AA";
    private static final String UPDATED_STATE_CODE = "BB";
    private static final String DEFAULT_STATE_NAME = "AAAAA";
    private static final String UPDATED_STATE_NAME = "BBBBB";

    @Inject
    private LookupStateRepository lookupStateRepository;

    @Inject
    private LookupStateSearchRepository lookupStateSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLookupStateMockMvc;

    private LookupState lookupState;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LookupStateResource lookupStateResource = new LookupStateResource();
        ReflectionTestUtils.setField(lookupStateResource, "lookupStateSearchRepository", lookupStateSearchRepository);
        ReflectionTestUtils.setField(lookupStateResource, "lookupStateRepository", lookupStateRepository);
        this.restLookupStateMockMvc = MockMvcBuilders.standaloneSetup(lookupStateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        lookupState = new LookupState();
        lookupState.setStateCode(DEFAULT_STATE_CODE);
        lookupState.setStateName(DEFAULT_STATE_NAME);
    }

    @Test
    @Transactional
    public void createLookupState() throws Exception {
        int databaseSizeBeforeCreate = lookupStateRepository.findAll().size();

        // Create the LookupState

        restLookupStateMockMvc.perform(post("/api/lookupStates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupState)))
                .andExpect(status().isCreated());

        // Validate the LookupState in the database
        List<LookupState> lookupStates = lookupStateRepository.findAll();
        assertThat(lookupStates).hasSize(databaseSizeBeforeCreate + 1);
        LookupState testLookupState = lookupStates.get(lookupStates.size() - 1);
        assertThat(testLookupState.getStateCode()).isEqualTo(DEFAULT_STATE_CODE);
        assertThat(testLookupState.getStateName()).isEqualTo(DEFAULT_STATE_NAME);
    }

    @Test
    @Transactional
    public void checkStateCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = lookupStateRepository.findAll().size();
        // set the field null
        lookupState.setStateCode(null);

        // Create the LookupState, which fails.

        restLookupStateMockMvc.perform(post("/api/lookupStates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupState)))
                .andExpect(status().isBadRequest());

        List<LookupState> lookupStates = lookupStateRepository.findAll();
        assertThat(lookupStates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStateNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = lookupStateRepository.findAll().size();
        // set the field null
        lookupState.setStateName(null);

        // Create the LookupState, which fails.

        restLookupStateMockMvc.perform(post("/api/lookupStates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupState)))
                .andExpect(status().isBadRequest());

        List<LookupState> lookupStates = lookupStateRepository.findAll();
        assertThat(lookupStates).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLookupStates() throws Exception {
        // Initialize the database
        lookupStateRepository.saveAndFlush(lookupState);

        // Get all the lookupStates
        restLookupStateMockMvc.perform(get("/api/lookupStates?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lookupState.getId().intValue())))
                .andExpect(jsonPath("$.[*].stateCode").value(hasItem(DEFAULT_STATE_CODE.toString())))
                .andExpect(jsonPath("$.[*].stateName").value(hasItem(DEFAULT_STATE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getLookupState() throws Exception {
        // Initialize the database
        lookupStateRepository.saveAndFlush(lookupState);

        // Get the lookupState
        restLookupStateMockMvc.perform(get("/api/lookupStates/{id}", lookupState.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(lookupState.getId().intValue()))
            .andExpect(jsonPath("$.stateCode").value(DEFAULT_STATE_CODE.toString()))
            .andExpect(jsonPath("$.stateName").value(DEFAULT_STATE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLookupState() throws Exception {
        // Get the lookupState
        restLookupStateMockMvc.perform(get("/api/lookupStates/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLookupState() throws Exception {
        // Initialize the database
        lookupStateRepository.saveAndFlush(lookupState);

		int databaseSizeBeforeUpdate = lookupStateRepository.findAll().size();

        // Update the lookupState
        lookupState.setStateCode(UPDATED_STATE_CODE);
        lookupState.setStateName(UPDATED_STATE_NAME);

        restLookupStateMockMvc.perform(put("/api/lookupStates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupState)))
                .andExpect(status().isOk());

        // Validate the LookupState in the database
        List<LookupState> lookupStates = lookupStateRepository.findAll();
        assertThat(lookupStates).hasSize(databaseSizeBeforeUpdate);
        LookupState testLookupState = lookupStates.get(lookupStates.size() - 1);
        assertThat(testLookupState.getStateCode()).isEqualTo(UPDATED_STATE_CODE);
        assertThat(testLookupState.getStateName()).isEqualTo(UPDATED_STATE_NAME);
    }

    @Test
    @Transactional
    public void deleteLookupState() throws Exception {
        // Initialize the database
        lookupStateRepository.saveAndFlush(lookupState);

		int databaseSizeBeforeDelete = lookupStateRepository.findAll().size();

        // Get the lookupState
        restLookupStateMockMvc.perform(delete("/api/lookupStates/{id}", lookupState.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<LookupState> lookupStates = lookupStateRepository.findAll();
        assertThat(lookupStates).hasSize(databaseSizeBeforeDelete - 1);
    }
}
