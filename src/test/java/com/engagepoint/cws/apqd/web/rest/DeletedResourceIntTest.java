package com.engagepoint.cws.apqd.web.rest;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.domain.Deleted;
import com.engagepoint.cws.apqd.repository.DeletedRepository;
import com.engagepoint.cws.apqd.repository.search.DeletedSearchRepository;

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
 * Test class for the DeletedResource REST controller.
 *
 * @see DeletedResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DeletedResourceIntTest {


    @Inject
    private DeletedRepository deletedRepository;

    @Inject
    private DeletedSearchRepository deletedSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDeletedMockMvc;

    private Deleted deleted;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DeletedResource deletedResource = new DeletedResource();
        ReflectionTestUtils.setField(deletedResource, "deletedSearchRepository", deletedSearchRepository);
        ReflectionTestUtils.setField(deletedResource, "deletedRepository", deletedRepository);
        this.restDeletedMockMvc = MockMvcBuilders.standaloneSetup(deletedResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        deleted = new Deleted();
    }

    @Test
    @Transactional
    public void createDeleted() throws Exception {
        int databaseSizeBeforeCreate = deletedRepository.findAll().size();

        // Create the Deleted

        restDeletedMockMvc.perform(post("/api/deleteds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deleted)))
                .andExpect(status().isCreated());

        // Validate the Deleted in the database
        List<Deleted> deleteds = deletedRepository.findAll();
        assertThat(deleteds).hasSize(databaseSizeBeforeCreate + 1);
        Deleted testDeleted = deleteds.get(deleteds.size() - 1);
    }

    @Test
    @Transactional
    public void getAllDeleteds() throws Exception {
        // Initialize the database
        deletedRepository.saveAndFlush(deleted);

        // Get all the deleteds
        restDeletedMockMvc.perform(get("/api/deleteds?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(deleted.getId().intValue())));
    }

    @Test
    @Transactional
    public void getDeleted() throws Exception {
        // Initialize the database
        deletedRepository.saveAndFlush(deleted);

        // Get the deleted
        restDeletedMockMvc.perform(get("/api/deleteds/{id}", deleted.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(deleted.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingDeleted() throws Exception {
        // Get the deleted
        restDeletedMockMvc.perform(get("/api/deleteds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeleted() throws Exception {
        // Initialize the database
        deletedRepository.saveAndFlush(deleted);

		int databaseSizeBeforeUpdate = deletedRepository.findAll().size();

        // Update the deleted

        restDeletedMockMvc.perform(put("/api/deleteds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(deleted)))
                .andExpect(status().isOk());

        // Validate the Deleted in the database
        List<Deleted> deleteds = deletedRepository.findAll();
        assertThat(deleteds).hasSize(databaseSizeBeforeUpdate);
        Deleted testDeleted = deleteds.get(deleteds.size() - 1);
    }

    @Test
    @Transactional
    public void deleteDeleted() throws Exception {
        // Initialize the database
        deletedRepository.saveAndFlush(deleted);

		int databaseSizeBeforeDelete = deletedRepository.findAll().size();

        // Get the deleted
        restDeletedMockMvc.perform(delete("/api/deleteds/{id}", deleted.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Deleted> deleteds = deletedRepository.findAll();
        assertThat(deleteds).hasSize(databaseSizeBeforeDelete - 1);
    }
}
