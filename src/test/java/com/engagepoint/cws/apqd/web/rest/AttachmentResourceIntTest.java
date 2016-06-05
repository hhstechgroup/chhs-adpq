package com.engagepoint.cws.apqd.web.rest;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.domain.Attachment;
import com.engagepoint.cws.apqd.repository.AttachmentRepository;
import com.engagepoint.cws.apqd.repository.search.AttachmentSearchRepository;

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


/**
 * Test class for the AttachmentResource REST controller.
 *
 * @see AttachmentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AttachmentResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_FILE_NAME = "AAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBB";
    private static final String DEFAULT_FILE_MIME_TYPE = "AAAAA";
    private static final String UPDATED_FILE_MIME_TYPE = "BBBBB";

    private static final Integer DEFAULT_FILE_SIZE = 1;
    private static final Integer UPDATED_FILE_SIZE = 2;
    private static final String DEFAULT_FILE_DESCRIPTION = "AAAAA";
    private static final String UPDATED_FILE_DESCRIPTION = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATION_DATE_STR = dateTimeFormatter.format(DEFAULT_CREATION_DATE);

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    @Inject
    private AttachmentRepository attachmentRepository;

    @Inject
    private AttachmentSearchRepository attachmentSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAttachmentMockMvc;

    private Attachment attachment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AttachmentResource attachmentResource = new AttachmentResource();
        ReflectionTestUtils.setField(attachmentResource, "attachmentSearchRepository", attachmentSearchRepository);
        ReflectionTestUtils.setField(attachmentResource, "attachmentRepository", attachmentRepository);
        this.restAttachmentMockMvc = MockMvcBuilders.standaloneSetup(attachmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        attachment = new Attachment();
        attachment.setFileName(DEFAULT_FILE_NAME);
        attachment.setFileMimeType(DEFAULT_FILE_MIME_TYPE);
        attachment.setFileSize(DEFAULT_FILE_SIZE);
        attachment.setFileDescription(DEFAULT_FILE_DESCRIPTION);
        attachment.setCreationDate(DEFAULT_CREATION_DATE);
        attachment.setFile(DEFAULT_FILE);
        attachment.setFileContentType(DEFAULT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createAttachment() throws Exception {
        int databaseSizeBeforeCreate = attachmentRepository.findAll().size();

        // Create the Attachment

        restAttachmentMockMvc.perform(post("/api/attachments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(attachment)))
                .andExpect(status().isCreated());

        // Validate the Attachment in the database
        List<Attachment> attachments = attachmentRepository.findAll();
        assertThat(attachments).hasSize(databaseSizeBeforeCreate + 1);
        Attachment testAttachment = attachments.get(attachments.size() - 1);
        assertThat(testAttachment.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testAttachment.getFileMimeType()).isEqualTo(DEFAULT_FILE_MIME_TYPE);
        assertThat(testAttachment.getFileSize()).isEqualTo(DEFAULT_FILE_SIZE);
        assertThat(testAttachment.getFileDescription()).isEqualTo(DEFAULT_FILE_DESCRIPTION);
        assertThat(testAttachment.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testAttachment.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void getAllAttachments() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get all the attachments
        restAttachmentMockMvc.perform(get("/api/attachments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(attachment.getId().intValue())))
                .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME.toString())))
                .andExpect(jsonPath("$.[*].fileMimeType").value(hasItem(DEFAULT_FILE_MIME_TYPE.toString())))
                .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE)))
                .andExpect(jsonPath("$.[*].fileDescription").value(hasItem(DEFAULT_FILE_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)));
    }

    @Test
    @Transactional
    public void getAttachment() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

        // Get the attachment
        restAttachmentMockMvc.perform(get("/api/attachments/{id}", attachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(attachment.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME.toString()))
            .andExpect(jsonPath("$.fileMimeType").value(DEFAULT_FILE_MIME_TYPE.toString()))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE))
            .andExpect(jsonPath("$.fileDescription").value(DEFAULT_FILE_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE_STR))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE));
    }

    @Test
    @Transactional
    public void getNonExistingAttachment() throws Exception {
        // Get the attachment
        restAttachmentMockMvc.perform(get("/api/attachments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttachment() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

		int databaseSizeBeforeUpdate = attachmentRepository.findAll().size();

        // Update the attachment
        attachment.setFileName(UPDATED_FILE_NAME);
        attachment.setFileMimeType(UPDATED_FILE_MIME_TYPE);
        attachment.setFileSize(UPDATED_FILE_SIZE);
        attachment.setFileDescription(UPDATED_FILE_DESCRIPTION);
        attachment.setCreationDate(UPDATED_CREATION_DATE);
        attachment.setFile(UPDATED_FILE);
        attachment.setFileContentType(UPDATED_FILE_CONTENT_TYPE);

        restAttachmentMockMvc.perform(put("/api/attachments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(attachment)))
                .andExpect(status().isOk());

        // Validate the Attachment in the database
        List<Attachment> attachments = attachmentRepository.findAll();
        assertThat(attachments).hasSize(databaseSizeBeforeUpdate);
        Attachment testAttachment = attachments.get(attachments.size() - 1);
        assertThat(testAttachment.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testAttachment.getFileMimeType()).isEqualTo(UPDATED_FILE_MIME_TYPE);
        assertThat(testAttachment.getFileSize()).isEqualTo(UPDATED_FILE_SIZE);
        assertThat(testAttachment.getFileDescription()).isEqualTo(UPDATED_FILE_DESCRIPTION);
        assertThat(testAttachment.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testAttachment.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void deleteAttachment() throws Exception {
        // Initialize the database
        attachmentRepository.saveAndFlush(attachment);

		int databaseSizeBeforeDelete = attachmentRepository.findAll().size();

        // Get the attachment
        restAttachmentMockMvc.perform(delete("/api/attachments/{id}", attachment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Attachment> attachments = attachmentRepository.findAll();
        assertThat(attachments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
