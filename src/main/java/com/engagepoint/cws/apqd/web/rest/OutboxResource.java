package com.engagepoint.cws.apqd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.engagepoint.cws.apqd.domain.Outbox;
import com.engagepoint.cws.apqd.repository.OutboxRepository;
import com.engagepoint.cws.apqd.repository.search.OutboxSearchRepository;
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
 * REST controller for managing Outbox.
 */
@RestController
@RequestMapping("/api")
public class OutboxResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutboxResource.class);

    @Inject
    private OutboxRepository outboxRepository;

    @Inject
    private OutboxSearchRepository outboxSearchRepository;

    /**
     * POST  /outboxs -> Create a new outbox.
     */
    @RequestMapping(value = "/outboxs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Outbox> createOutbox(@RequestBody Outbox outbox) throws URISyntaxException {
        LOGGER.debug("REST request to save Outbox : {}", outbox);
        if (outbox.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("outbox", "idexists", "A new outbox cannot already have an ID")).body(null);
        }
        Outbox result = outboxRepository.save(outbox);
        outboxSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/outboxs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("outbox", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /outboxs -> Updates an existing outbox.
     */
    @RequestMapping(value = "/outboxs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Outbox> updateOutbox(@RequestBody Outbox outbox) throws URISyntaxException {
        LOGGER.debug("REST request to update Outbox : {}", outbox);
        if (outbox.getId() == null) {
            return createOutbox(outbox);
        }
        Outbox result = outboxRepository.save(outbox);
        outboxSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("outbox", outbox.getId().toString()))
            .body(result);
    }

    /**
     * GET  /outboxs -> get all the outboxs.
     */
    @RequestMapping(value = "/outboxs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Outbox> getAllOutboxs(@RequestParam(required = false) String filter) {
        if ("mailbox-is-null".equals(filter)) {
            LOGGER.debug("REST request to get all Outboxs where mailBox is null");
            return StreamSupport
                .stream(outboxRepository.findAll().spliterator(), false)
                .filter(outbox -> outbox.getMailBox() == null)
                .collect(Collectors.toList());
        }
        LOGGER.debug("REST request to get all Outboxs");
        return outboxRepository.findAll();
            }

    /**
     * GET  /outboxs/:id -> get the "id" outbox.
     */
    @RequestMapping(value = "/outboxs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Outbox> getOutbox(@PathVariable Long id) {
        LOGGER.debug("REST request to get Outbox : {}", id);
        Outbox outbox = outboxRepository.findOne(id);
        return Optional.ofNullable(outbox)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /outboxs/:id -> delete the "id" outbox.
     */
    @RequestMapping(value = "/outboxs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOutbox(@PathVariable Long id) {
        LOGGER.debug("REST request to delete Outbox : {}", id);
        outboxRepository.delete(id);
        outboxSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("outbox", id.toString())).build();
    }

    /**
     * SEARCH  /_search/outboxs/:query -> search for the outbox corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/outboxs/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Outbox> searchOutboxs(@PathVariable String query) {
        LOGGER.debug("REST request to search Outboxs for query {}", query);
        return StreamSupport
            .stream(outboxSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
