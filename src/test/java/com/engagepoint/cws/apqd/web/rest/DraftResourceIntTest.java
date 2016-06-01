package com.engagepoint.cws.apqd.web.rest;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.domain.Draft;
import com.engagepoint.cws.apqd.repository.DraftRepository;
import com.engagepoint.cws.apqd.repository.search.DraftSearchRepository;

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
 * Test class for the DraftResource REST controller.
 *
 * @see DraftResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DraftResourceIntTest {


    @Inject
    private DraftRepository draftRepository;

    @Inject
    private DraftSearchRepository draftSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDraftMockMvc;

    private Draft draft;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DraftResource draftResource = new DraftResource();
        ReflectionTestUtils.setField(draftResource, "draftSearchRepository", draftSearchRepository);
        ReflectionTestUtils.setField(draftResource, "draftRepository", draftRepository);
        this.restDraftMockMvc = MockMvcBuilders.standaloneSetup(draftResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        draft = new Draft();
    }

    @Test
    @Transactional
    public void createDraft() throws Exception {
        int databaseSizeBeforeCreate = draftRepository.findAll().size();

        // Create the Draft

        restDraftMockMvc.perform(post("/api/drafts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(draft)))
                .andExpect(status().isCreated());

        // Validate the Draft in the database
        List<Draft> drafts = draftRepository.findAll();
        assertThat(drafts).hasSize(databaseSizeBeforeCreate + 1);
        Draft testDraft = drafts.get(drafts.size() - 1);
    }

    @Test
    @Transactional
    public void getAllDrafts() throws Exception {
        // Initialize the database
        draftRepository.saveAndFlush(draft);

        // Get all the drafts
        restDraftMockMvc.perform(get("/api/drafts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(draft.getId().intValue())));
    }

    @Test
    @Transactional
    public void getDraft() throws Exception {
        // Initialize the database
        draftRepository.saveAndFlush(draft);

        // Get the draft
        restDraftMockMvc.perform(get("/api/drafts/{id}", draft.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(draft.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDraft() throws Exception {
        // Get the draft
        restDraftMockMvc.perform(get("/api/drafts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDraft() throws Exception {
        // Initialize the database
        draftRepository.saveAndFlush(draft);

		int databaseSizeBeforeUpdate = draftRepository.findAll().size();

        // Update the draft

        restDraftMockMvc.perform(put("/api/drafts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(draft)))
                .andExpect(status().isOk());

        // Validate the Draft in the database
        List<Draft> drafts = draftRepository.findAll();
        assertThat(drafts).hasSize(databaseSizeBeforeUpdate);
        Draft testDraft = drafts.get(drafts.size() - 1);
    }

    @Test
    @Transactional
    public void deleteDraft() throws Exception {
        // Initialize the database
        draftRepository.saveAndFlush(draft);

		int databaseSizeBeforeDelete = draftRepository.findAll().size();

        // Get the draft
        restDraftMockMvc.perform(delete("/api/drafts/{id}", draft.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Draft> drafts = draftRepository.findAll();
        assertThat(drafts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
