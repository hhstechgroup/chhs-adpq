package com.engagepoint.cws.apqd.domain;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.repository.AttachmentRepository;
import com.engagepoint.cws.apqd.repository.MessageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import java.time.ZonedDateTime;

import static com.engagepoint.cws.apqd.APQDTestUtil.assertObjectIdentity;
import static com.engagepoint.cws.apqd.APQDTestUtil.prepareMessage;
import static org.assertj.core.api.StrictAssertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AttachmentTest {
    @Inject
    private AttachmentRepository attachmentRepository;

    @Inject
    private MessageRepository messageRepository;

    private Attachment createEntity(String index) {
        Attachment attachment = new Attachment();

        attachment.setCreationDate(ZonedDateTime.now());
        attachment.setFileName("fileName" + index);
        attachment.setFileMimeType("fileMimeType" + index);
        attachment.setFileDescription("fileDescription" + index);
        attachment.setFileContentType("video/3gpp");
        attachment.setFile(("content" + index).getBytes());
        attachment.setFileSize(attachment.getFile().length);

        attachment.setMessage(prepareMessage(messageRepository, "messageSubject" + index, "messageBody" + index, null, null));

        return attachmentRepository.saveAndFlush(attachment);
    }

    @Test
    @Transactional
    public void testEntityFields() throws Exception {
        Attachment attachment = createEntity("0");
        Message message = attachment.getMessage();

        Attachment testAttachment = attachmentRepository.findOne(attachment.getId());
        assertThat(testAttachment).isNotNull();
        assertThat(testAttachment.getCreationDate()).isNotNull();
        assertThat(testAttachment.getFileName()).isEqualTo(attachment.getFileName());
        assertThat(testAttachment.getFileMimeType()).isEqualTo(attachment.getFileMimeType());
        assertThat(testAttachment.getFileDescription()).isEqualTo(attachment.getFileDescription());
        assertThat(testAttachment.getFileContentType()).isEqualTo(attachment.getFileContentType());
        assertThat(testAttachment.getFile()).isEqualTo(attachment.getFile());
        assertThat(testAttachment.getFileSize()).isEqualTo(attachment.getFileSize());

        Message testMessage = testAttachment.getMessage();
        assertThat(testMessage.getSubject()).isEqualTo(message.getSubject());
        assertThat(testMessage.getBody()).isEqualTo(message.getBody());
    }

    @Test
    @Transactional
    public void testIdentity() throws Exception {
        Attachment attachment1 = createEntity("1");
        Attachment attachment2 = createEntity("2");
        Attachment foundEntity = attachmentRepository.findOne(attachment2.getId());

        assertObjectIdentity(attachment1, attachment2, foundEntity, null);
    }
}
