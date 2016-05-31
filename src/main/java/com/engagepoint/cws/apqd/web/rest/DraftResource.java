package com.engagepoint.cws.apqd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.engagepoint.cws.apqd.domain.Draft;
import com.engagepoint.cws.apqd.repository.DraftRepository;
import com.engagepoint.cws.apqd.repository.search.DraftSearchRepository;
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
 * REST controller for managing Draft.
 */
@RestController
@RequestMapping("/api")
public class DraftResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DraftResource.class);

    @Inject
    private DraftRepository draftRepository;

    @Inject
    private DraftSearchRepository draftSearchRepository;

    /**
     * POST  /drafts -> Create a new draft.
     */
    @RequestMapping(value = "/drafts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Draft> createDraft(@RequestBody Draft draft) throws URISyntaxException {
        LOGGER.debug("REST request to save Draft : {}", draft);
        if (draft.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("draft", "idexists", "A new draft cannot already have an ID")).body(null);
        }
        Draft result = draftRepository.save(draft);
        draftSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/drafts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("draft", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /drafts -> Updates an existing draft.
     */
    @RequestMapping(value = "/drafts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Draft> updateDraft(@RequestBody Draft draft) throws URISyntaxException {
        LOGGER.debug("REST request to update Draft : {}", draft);
        if (draft.getId() == null) {
            return createDraft(draft);
        }
        Draft result = draftRepository.save(draft);
        draftSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("draft", draft.getId().toString()))
            .body(result);
    }

    /**
     * GET  /drafts -> get all the drafts.
     */
    @RequestMapping(value = "/drafts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Draft> getAllDrafts(@RequestParam(required = false) String filter) {
        if ("mailbox-is-null".equals(filter)) {
            LOGGER.debug("REST request to get all Drafts where mailBox is null");
            return StreamSupport
                .stream(draftRepository.findAll().spliterator(), false)
                .filter(draft -> draft.getMailBox() == null)
                .collect(Collectors.toList());
        }
        LOGGER.debug("REST request to get all Drafts");
        return draftRepository.findAll();
            }

    /**
     * GET  /drafts/:id -> get the "id" draft.
     */
    @RequestMapping(value = "/drafts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Draft> getDraft(@PathVariable Long id) {
        LOGGER.debug("REST request to get Draft : {}", id);
        Draft draft = draftRepository.findOne(id);
        return Optional.ofNullable(draft)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /drafts/:id -> delete the "id" draft.
     */
    @RequestMapping(value = "/drafts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDraft(@PathVariable Long id) {
        LOGGER.debug("REST request to delete Draft : {}", id);
        draftRepository.delete(id);
        draftSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("draft", id.toString())).build();
    }

    /**
     * SEARCH  /_search/drafts/:query -> search for the draft corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/drafts/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Draft> searchDrafts(@PathVariable String query) {
        LOGGER.debug("REST request to search Drafts for query {}", query);
        return StreamSupport
            .stream(draftSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
