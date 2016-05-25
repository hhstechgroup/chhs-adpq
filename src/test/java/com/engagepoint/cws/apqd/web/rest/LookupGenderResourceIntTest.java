package com.engagepoint.cws.apqd.web.rest;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.domain.LookupGender;
import com.engagepoint.cws.apqd.repository.LookupGenderRepository;
import com.engagepoint.cws.apqd.repository.search.LookupGenderSearchRepository;

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
 * Test class for the LookupGenderResource REST controller.
 *
 * @see LookupGenderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class LookupGenderResourceIntTest {

    private static final String DEFAULT_GENDER_CODE = "A";
    private static final String UPDATED_GENDER_CODE = "B";
    private static final String DEFAULT_GENDER_NAME = "AAAAA";
    private static final String UPDATED_GENDER_NAME = "BBBBB";

    @Inject
    private LookupGenderRepository lookupGenderRepository;

    @Inject
    private LookupGenderSearchRepository lookupGenderSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLookupGenderMockMvc;

    private LookupGender lookupGender;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LookupGenderResource lookupGenderResource = new LookupGenderResource();
        ReflectionTestUtils.setField(lookupGenderResource, "lookupGenderSearchRepository", lookupGenderSearchRepository);
        ReflectionTestUtils.setField(lookupGenderResource, "lookupGenderRepository", lookupGenderRepository);
        this.restLookupGenderMockMvc = MockMvcBuilders.standaloneSetup(lookupGenderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        lookupGender = new LookupGender();
        lookupGender.setGenderCode(DEFAULT_GENDER_CODE);
        lookupGender.setGenderName(DEFAULT_GENDER_NAME);
    }

    @Test
    @Transactional
    public void createLookupGender() throws Exception {
        int databaseSizeBeforeCreate = lookupGenderRepository.findAll().size();

        // Create the LookupGender

        restLookupGenderMockMvc.perform(post("/api/lookupGenders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupGender)))
                .andExpect(status().isCreated());

        // Validate the LookupGender in the database
        List<LookupGender> lookupGenders = lookupGenderRepository.findAll();
        assertThat(lookupGenders).hasSize(databaseSizeBeforeCreate + 1);
        LookupGender testLookupGender = lookupGenders.get(lookupGenders.size() - 1);
        assertThat(testLookupGender.getGenderCode()).isEqualTo(DEFAULT_GENDER_CODE);
        assertThat(testLookupGender.getGenderName()).isEqualTo(DEFAULT_GENDER_NAME);
    }

    @Test
    @Transactional
    public void checkGenderCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = lookupGenderRepository.findAll().size();
        // set the field null
        lookupGender.setGenderCode(null);

        // Create the LookupGender, which fails.

        restLookupGenderMockMvc.perform(post("/api/lookupGenders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupGender)))
                .andExpect(status().isBadRequest());

        List<LookupGender> lookupGenders = lookupGenderRepository.findAll();
        assertThat(lookupGenders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGenderNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = lookupGenderRepository.findAll().size();
        // set the field null
        lookupGender.setGenderName(null);

        // Create the LookupGender, which fails.

        restLookupGenderMockMvc.perform(post("/api/lookupGenders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupGender)))
                .andExpect(status().isBadRequest());

        List<LookupGender> lookupGenders = lookupGenderRepository.findAll();
        assertThat(lookupGenders).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLookupGenders() throws Exception {
        // Initialize the database
        lookupGenderRepository.saveAndFlush(lookupGender);

        // Get all the lookupGenders
        restLookupGenderMockMvc.perform(get("/api/lookupGenders?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lookupGender.getId().intValue())))
                .andExpect(jsonPath("$.[*].genderCode").value(hasItem(DEFAULT_GENDER_CODE.toString())))
                .andExpect(jsonPath("$.[*].genderName").value(hasItem(DEFAULT_GENDER_NAME.toString())));
    }

    @Test
    @Transactional
    public void getLookupGender() throws Exception {
        // Initialize the database
        lookupGenderRepository.saveAndFlush(lookupGender);

        // Get the lookupGender
        restLookupGenderMockMvc.perform(get("/api/lookupGenders/{id}", lookupGender.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(lookupGender.getId().intValue()))
            .andExpect(jsonPath("$.genderCode").value(DEFAULT_GENDER_CODE.toString()))
            .andExpect(jsonPath("$.genderName").value(DEFAULT_GENDER_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLookupGender() throws Exception {
        // Get the lookupGender
        restLookupGenderMockMvc.perform(get("/api/lookupGenders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLookupGender() throws Exception {
        // Initialize the database
        lookupGenderRepository.saveAndFlush(lookupGender);

		int databaseSizeBeforeUpdate = lookupGenderRepository.findAll().size();

        // Update the lookupGender
        lookupGender.setGenderCode(UPDATED_GENDER_CODE);
        lookupGender.setGenderName(UPDATED_GENDER_NAME);

        restLookupGenderMockMvc.perform(put("/api/lookupGenders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lookupGender)))
                .andExpect(status().isOk());

        // Validate the LookupGender in the database
        List<LookupGender> lookupGenders = lookupGenderRepository.findAll();
        assertThat(lookupGenders).hasSize(databaseSizeBeforeUpdate);
        LookupGender testLookupGender = lookupGenders.get(lookupGenders.size() - 1);
        assertThat(testLookupGender.getGenderCode()).isEqualTo(UPDATED_GENDER_CODE);
        assertThat(testLookupGender.getGenderName()).isEqualTo(UPDATED_GENDER_NAME);
    }

    @Test
    @Transactional
    public void deleteLookupGender() throws Exception {
        // Initialize the database
        lookupGenderRepository.saveAndFlush(lookupGender);

		int databaseSizeBeforeDelete = lookupGenderRepository.findAll().size();

        // Get the lookupGender
        restLookupGenderMockMvc.perform(delete("/api/lookupGenders/{id}", lookupGender.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<LookupGender> lookupGenders = lookupGenderRepository.findAll();
        assertThat(lookupGenders).hasSize(databaseSizeBeforeDelete - 1);
    }
}
