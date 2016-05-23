package com.engagepoint.cws.apqd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.engagepoint.cws.apqd.domain.Email;
import com.engagepoint.cws.apqd.repository.EmailRepository;
import com.engagepoint.cws.apqd.repository.search.EmailSearchRepository;
import com.engagepoint.cws.apqd.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Email.
 */
@RestController
@RequestMapping("/api")
public class EmailResource {

    private final Logger log = LoggerFactory.getLogger(EmailResource.class);
        
    @Inject
    private EmailRepository emailRepository;
    
    @Inject
    private EmailSearchRepository emailSearchRepository;
    
    /**
     * POST  /emails -> Create a new email.
     */
    @RequestMapping(value = "/emails",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Email> createEmail(@Valid @RequestBody Email email) throws URISyntaxException {
        log.debug("REST request to save Email : {}", email);
        if (email.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("email", "idexists", "A new email cannot already have an ID")).body(null);
        }
        Email result = emailRepository.save(email);
        emailSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/emails/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("email", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /emails -> Updates an existing email.
     */
    @RequestMapping(value = "/emails",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Email> updateEmail(@Valid @RequestBody Email email) throws URISyntaxException {
        log.debug("REST request to update Email : {}", email);
        if (email.getId() == null) {
            return createEmail(email);
        }
        Email result = emailRepository.save(email);
        emailSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("email", email.getId().toString()))
            .body(result);
    }

    /**
     * GET  /emails -> get all the emails.
     */
    @RequestMapping(value = "/emails",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Email> getAllEmails() {
        log.debug("REST request to get all Emails");
        return emailRepository.findAll();
            }

    /**
     * GET  /emails/:id -> get the "id" email.
     */
    @RequestMapping(value = "/emails/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Email> getEmail(@PathVariable Long id) {
        log.debug("REST request to get Email : {}", id);
        Email email = emailRepository.findOne(id);
        return Optional.ofNullable(email)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /emails/:id -> delete the "id" email.
     */
    @RequestMapping(value = "/emails/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        log.debug("REST request to delete Email : {}", id);
        emailRepository.delete(id);
        emailSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("email", id.toString())).build();
    }

    /**
     * SEARCH  /_search/emails/:query -> search for the email corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/emails/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Email> searchEmails(@PathVariable String query) {
        log.debug("REST request to search Emails for query {}", query);
        return StreamSupport
            .stream(emailSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
