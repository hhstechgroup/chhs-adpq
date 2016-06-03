package com.engagepoint.cws.apqd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.engagepoint.cws.apqd.domain.Inbox;
import com.engagepoint.cws.apqd.repository.InboxRepository;
import com.engagepoint.cws.apqd.repository.search.InboxSearchRepository;
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
 * REST controller for managing Inbox.
 */
@RestController
@RequestMapping("/api")
public class InboxResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(InboxResource.class);

    @Inject
    private InboxRepository inboxRepository;

    @Inject
    private InboxSearchRepository inboxSearchRepository;

    /**
     * POST  /inboxs -> Create a new inbox.
     */
    @RequestMapping(value = "/inboxs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Inbox> createInbox(@RequestBody Inbox inbox) throws URISyntaxException {
        LOGGER.debug("REST request to save Inbox : {}", inbox);
        if (inbox.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("inbox", "idexists", "A new inbox cannot already have an ID")).body(null);
        }
        Inbox result = inboxRepository.save(inbox);
        inboxSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/inboxs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("inbox", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /inboxs -> Updates an existing inbox.
     */
    @RequestMapping(value = "/inboxs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Inbox> updateInbox(@RequestBody Inbox inbox) throws URISyntaxException {
        LOGGER.debug("REST request to update Inbox : {}", inbox);
        if (inbox.getId() == null) {
            return createInbox(inbox);
        }
        Inbox result = inboxRepository.save(inbox);
        inboxSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("inbox", inbox.getId().toString()))
            .body(result);
    }

    /**
     * GET  /inboxs -> get all the inboxs.
     */
    @RequestMapping(value = "/inboxs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Inbox> getAllInboxs(@RequestParam(required = false) String filter) {
        if ("mailbox-is-null".equals(filter)) {
            LOGGER.debug("REST request to get all Inboxs where mailBox is null");
            return StreamSupport
                .stream(inboxRepository.findAll().spliterator(), false)
                .filter(inbox -> inbox.getMailBox() == null)
                .collect(Collectors.toList());
        }
        LOGGER.debug("REST request to get all Inboxs");
        return inboxRepository.findAll();
    }

    /**
     * GET  /inboxs/:id -> get the "id" inbox.
     */
    @RequestMapping(value = "/inboxs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Inbox> getInbox(@PathVariable Long id) {
        LOGGER.debug("REST request to get Inbox : {}", id);
        Inbox inbox = inboxRepository.findOne(id);
        return Optional.ofNullable(inbox)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /inboxs/:id -> delete the "id" inbox.
     */
    @RequestMapping(value = "/inboxs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInbox(@PathVariable Long id) {
        LOGGER.debug("REST request to delete Inbox : {}", id);
        inboxRepository.delete(id);
        inboxSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("inbox", id.toString())).build();
    }

    /**
     * SEARCH  /_search/inboxs/:query -> search for the inbox corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/inboxs/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Inbox> searchInboxs(@PathVariable String query) {
        LOGGER.debug("REST request to search Inboxs for query {}", query);
        return StreamSupport
            .stream(inboxSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
