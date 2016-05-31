package com.engagepoint.cws.apqd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.engagepoint.cws.apqd.domain.LookupMaritalStatus;
import com.engagepoint.cws.apqd.repository.LookupMaritalStatusRepository;
import com.engagepoint.cws.apqd.repository.search.LookupMaritalStatusSearchRepository;
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
 * REST controller for managing LookupMaritalStatus.
 */
@RestController
@RequestMapping("/api")
public class LookupMaritalStatusResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(LookupMaritalStatusResource.class);

    @Inject
    private LookupMaritalStatusRepository lookupMaritalStatusRepository;

    @Inject
    private LookupMaritalStatusSearchRepository lookupMaritalStatusSearchRepository;

    /**
     * POST  /lookupMaritalStatuss -> Create a new lookupMaritalStatus.
     */
    @RequestMapping(value = "/lookupMaritalStatuss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LookupMaritalStatus> createLookupMaritalStatus(@Valid @RequestBody LookupMaritalStatus lookupMaritalStatus) throws URISyntaxException {
        LOGGER.debug("REST request to save LookupMaritalStatus : {}", lookupMaritalStatus);
        if (lookupMaritalStatus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lookupMaritalStatus", "idexists", "A new lookupMaritalStatus cannot already have an ID")).body(null);
        }
        LookupMaritalStatus result = lookupMaritalStatusRepository.save(lookupMaritalStatus);
        lookupMaritalStatusSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/lookupMaritalStatuss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lookupMaritalStatus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lookupMaritalStatuss -> Updates an existing lookupMaritalStatus.
     */
    @RequestMapping(value = "/lookupMaritalStatuss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LookupMaritalStatus> updateLookupMaritalStatus(@Valid @RequestBody LookupMaritalStatus lookupMaritalStatus) throws URISyntaxException {
        LOGGER.debug("REST request to update LookupMaritalStatus : {}", lookupMaritalStatus);
        if (lookupMaritalStatus.getId() == null) {
            return createLookupMaritalStatus(lookupMaritalStatus);
        }
        LookupMaritalStatus result = lookupMaritalStatusRepository.save(lookupMaritalStatus);
        lookupMaritalStatusSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lookupMaritalStatus", lookupMaritalStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lookupMaritalStatuss -> get all the lookupMaritalStatuss.
     */
    @RequestMapping(value = "/lookupMaritalStatuss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LookupMaritalStatus> getAllLookupMaritalStatuss() {
        LOGGER.debug("REST request to get all LookupMaritalStatuss");
        return lookupMaritalStatusRepository.findAll();
            }

    /**
     * GET  /lookupMaritalStatuss/:id -> get the "id" lookupMaritalStatus.
     */
    @RequestMapping(value = "/lookupMaritalStatuss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LookupMaritalStatus> getLookupMaritalStatus(@PathVariable Long id) {
        LOGGER.debug("REST request to get LookupMaritalStatus : {}", id);
        LookupMaritalStatus lookupMaritalStatus = lookupMaritalStatusRepository.findOne(id);
        return Optional.ofNullable(lookupMaritalStatus)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lookupMaritalStatuss/:id -> delete the "id" lookupMaritalStatus.
     */
    @RequestMapping(value = "/lookupMaritalStatuss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLookupMaritalStatus(@PathVariable Long id) {
        LOGGER.debug("REST request to delete LookupMaritalStatus : {}", id);
        lookupMaritalStatusRepository.delete(id);
        lookupMaritalStatusSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lookupMaritalStatus", id.toString())).build();
    }

    /**
     * SEARCH  /_search/lookupMaritalStatuss/:query -> search for the lookupMaritalStatus corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/lookupMaritalStatuss/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LookupMaritalStatus> searchLookupMaritalStatuss(@PathVariable String query) {
        LOGGER.debug("REST request to search LookupMaritalStatuss for query {}", query);
        return StreamSupport
            .stream(lookupMaritalStatusSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
