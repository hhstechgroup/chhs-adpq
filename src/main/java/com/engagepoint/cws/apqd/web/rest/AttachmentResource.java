package com.engagepoint.cws.apqd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.engagepoint.cws.apqd.domain.Attachment;
import com.engagepoint.cws.apqd.repository.AttachmentRepository;
import com.engagepoint.cws.apqd.repository.search.AttachmentSearchRepository;
import com.engagepoint.cws.apqd.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Attachment.
 */
@RestController
@RequestMapping("/api")
public class AttachmentResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentResource.class);

    @Inject
    private AttachmentRepository attachmentRepository;

    @Inject
    private AttachmentSearchRepository attachmentSearchRepository;

    /**
     * POST  /attachments -> Create a new attachment.
     */
    @RequestMapping(value = "/attachments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Attachment> createAttachment(@RequestBody Attachment attachment) throws URISyntaxException {
        LOGGER.debug("REST request to save Attachment : {}", attachment);
        if (attachment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("attachment", "idexists", "A new attachment cannot already have an ID")).body(null);
        }
        Attachment result = attachmentRepository.save(attachment);
        attachmentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/attachments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("attachment", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /attachments -> Updates an existing attachment.
     */
    @RequestMapping(value = "/attachments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Attachment> updateAttachment(@RequestBody Attachment attachment) throws URISyntaxException {
        LOGGER.debug("REST request to update Attachment : {}", attachment);
        if (attachment.getId() == null) {
            return createAttachment(attachment);
        }
        Attachment result = attachmentRepository.save(attachment);
        attachmentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("attachment", attachment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /attachments -> get all the attachments.
     */
    @RequestMapping(value = "/attachments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Attachment> getAllAttachments() {
        LOGGER.debug("REST request to get all Attachments");
        return attachmentRepository.findAll();
            }

    /**
     * GET  /attachments/:id -> get the "id" attachment.
     */
    @RequestMapping(value = "/attachments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Attachment> getAttachment(@PathVariable Long id) {
        LOGGER.debug("REST request to get Attachment : {}", id);
        Attachment attachment = attachmentRepository.findOne(id);
        return Optional.ofNullable(attachment)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /attachments/:id -> delete the "id" attachment.
     */
    @RequestMapping(value = "/attachments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long id) {
        LOGGER.debug("REST request to delete Attachment : {}", id);
        attachmentRepository.delete(id);
        attachmentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("attachment", id.toString())).build();
    }

    /**
     * SEARCH  /_search/attachments/:query -> search for the attachment corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/attachments/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Attachment> searchAttachments(@PathVariable String query) {
        LOGGER.debug("REST request to search Attachments for query {}", query);
        return StreamSupport
            .stream(attachmentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
