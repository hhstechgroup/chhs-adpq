package com.engagepoint.cws.apqd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.engagepoint.cws.apqd.domain.MailBox;
import com.engagepoint.cws.apqd.repository.MailBoxRepository;
import com.engagepoint.cws.apqd.repository.search.MailBoxSearchRepository;
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
 * REST controller for managing MailBox.
 */
@RestController
@RequestMapping("/api")
public class MailBoxResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailBoxResource.class);

    @Inject
    private MailBoxRepository mailBoxRepository;

    @Inject
    private MailBoxSearchRepository mailBoxSearchRepository;

    /**
     * POST  /mailBoxs -> Create a new mailBox.
     */
    @RequestMapping(value = "/mailBoxs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MailBox> createMailBox(@RequestBody MailBox mailBox) throws URISyntaxException {
        LOGGER.debug("REST request to save MailBox : {}", mailBox);
        if (mailBox.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("mailBox", "idexists", "A new mailBox cannot already have an ID")).body(null);
        }
        MailBox result = mailBoxRepository.save(mailBox);
        mailBoxSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/mailBoxs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("mailBox", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /mailBoxs -> Updates an existing mailBox.
     */
    @RequestMapping(value = "/mailBoxs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MailBox> updateMailBox(@RequestBody MailBox mailBox) throws URISyntaxException {
        LOGGER.debug("REST request to update MailBox : {}", mailBox);
        if (mailBox.getId() == null) {
            return createMailBox(mailBox);
        }
        MailBox result = mailBoxRepository.save(mailBox);
        mailBoxSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("mailBox", mailBox.getId().toString()))
            .body(result);
    }

    /**
     * GET  /mailBoxs -> get all the mailBoxs.
     */
    @RequestMapping(value = "/mailBoxs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MailBox> getAllMailBoxs(@RequestParam(required = false) String filter) {
        if ("user-is-null".equals(filter)) {
            LOGGER.debug("REST request to get all MailBoxs where user is null");
            return StreamSupport
                .stream(mailBoxRepository.findAll().spliterator(), false)
                .filter(mailBox -> mailBox.getUser() == null)
                .collect(Collectors.toList());
        }
        LOGGER.debug("REST request to get all MailBoxs");
        return mailBoxRepository.findAll();
            }

    /**
     * GET  /mailBoxs/:id -> get the "id" mailBox.
     */
    @RequestMapping(value = "/mailBoxs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<MailBox> getMailBox(@PathVariable Long id) {
        LOGGER.debug("REST request to get MailBox : {}", id);
        MailBox mailBox = mailBoxRepository.findOne(id);
        return Optional.ofNullable(mailBox)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /mailBoxs/:id -> delete the "id" mailBox.
     */
    @RequestMapping(value = "/mailBoxs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteMailBox(@PathVariable Long id) {
        LOGGER.debug("REST request to delete MailBox : {}", id);
        mailBoxRepository.delete(id);
        mailBoxSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("mailBox", id.toString())).build();
    }

    /**
     * SEARCH  /_search/mailBoxs/:query -> search for the mailBox corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/mailBoxs/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<MailBox> searchMailBoxs(@PathVariable String query) {
        LOGGER.debug("REST request to search MailBoxs for query {}", query);
        return StreamSupport
            .stream(mailBoxSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
