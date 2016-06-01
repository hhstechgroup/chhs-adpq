package com.engagepoint.cws.apqd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.engagepoint.cws.apqd.domain.LookupState;
import com.engagepoint.cws.apqd.repository.LookupStateRepository;
import com.engagepoint.cws.apqd.repository.search.LookupStateSearchRepository;
import com.engagepoint.cws.apqd.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing LookupState.
 */
@RestController
@RequestMapping("/api")
public class LookupStateResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(LookupStateResource.class);

    @Inject
    private LookupStateRepository lookupStateRepository;

    @Inject
    private LookupStateSearchRepository lookupStateSearchRepository;

    /**
     * POST  /lookupStates -> Create a new lookupState.
     */
    @RequestMapping(value = "/lookupStates",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LookupState> createLookupState(@Valid @RequestBody LookupState lookupState) throws URISyntaxException {
        LOGGER.debug("REST request to save LookupState : {}", lookupState);
        if (lookupState.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lookupState", "idexists", "A new lookupState cannot already have an ID")).body(null);
        }
        LookupState result = lookupStateRepository.save(lookupState);
        lookupStateSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/lookupStates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lookupState", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lookupStates -> Updates an existing lookupState.
     */
    @RequestMapping(value = "/lookupStates",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LookupState> updateLookupState(@Valid @RequestBody LookupState lookupState) throws URISyntaxException {
        LOGGER.debug("REST request to update LookupState : {}", lookupState);
        if (lookupState.getId() == null) {
            return createLookupState(lookupState);
        }
        LookupState result = lookupStateRepository.save(lookupState);
        lookupStateSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lookupState", lookupState.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lookupStates -> get all the lookupStates.
     */
    @RequestMapping(value = "/lookupStates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LookupState> getAllLookupStates() {
        LOGGER.debug("REST request to get all LookupStates");
        return lookupStateRepository.findAll();
            }

    /**
     * GET  /lookupStates/:id -> get the "id" lookupState.
     */
    @RequestMapping(value = "/lookupStates/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LookupState> getLookupState(@PathVariable Long id) {
        LOGGER.debug("REST request to get LookupState : {}", id);
        LookupState lookupState = lookupStateRepository.findOne(id);
        return Optional.ofNullable(lookupState)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lookupStates/:id -> delete the "id" lookupState.
     */
    @RequestMapping(value = "/lookupStates/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLookupState(@PathVariable Long id) {
        LOGGER.debug("REST request to delete LookupState : {}", id);
        lookupStateRepository.delete(id);
        lookupStateSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lookupState", id.toString())).build();
    }

    /**
     * SEARCH  /_search/lookupStates/:query -> search for the lookupState corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/lookupStates/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LookupState> searchLookupStates(@PathVariable String query) {
        LOGGER.debug("REST request to search LookupStates for query {}", query);
        return StreamSupport
            .stream(lookupStateSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
