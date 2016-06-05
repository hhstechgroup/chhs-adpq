package com.engagepoint.cws.apqd.web.rest;

import com.engagepoint.cws.apqd.domain.Attachment;
import com.engagepoint.cws.apqd.domain.Message;
import com.engagepoint.cws.apqd.repository.AttachmentRepository;
import com.engagepoint.cws.apqd.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api")
public class FileResource {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @RequestMapping(value = "/file", method = RequestMethod.POST)
    public void uploadFile(
        @RequestParam(value = "file", required = true) MultipartFile multipartFile,
        @RequestParam(value = "messageId", required = true) Long messageId) throws IOException {

        Message message = messageRepository.findOne(messageId);

        Attachment attachment = new Attachment();
        attachment.setCreationDate(ZonedDateTime.now());
        attachment.setFile(multipartFile.getBytes());
        attachment.setFileSize(attachment.getFile().length);
        attachment.setFileContentType(multipartFile.getContentType());
        attachment.setFileName(multipartFile.getOriginalFilename());
        attachment.setMessage(message);
        message.getAttachments().add(attachment);

        attachmentRepository.save(attachment);
        messageRepository.save(message);
    }

    @RequestMapping(value = "/file/{attachmentId}", method = RequestMethod.GET)
    public HttpEntity<byte[]> getFile(@PathVariable Long attachmentId) {
        Attachment attachment = attachmentRepository.findOne(attachmentId);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Disposition", "attachment;filename=\"" + attachment.getFileName() + "\"");
        httpHeaders.add("Content-Type", attachment.getFileContentType());
        httpHeaders.add("Content-Length", attachment.getFileSize().toString());

        return new ResponseEntity<>(attachment.getFile(), httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/file/{attachmentId:.+}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteFile(@PathVariable Long attachmentId) {
        Attachment attachment = attachmentRepository.findOne(attachmentId);

        Message message = attachment.getMessage();
        message.getAttachments().remove(attachment);
        attachmentRepository.delete(attachment);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

