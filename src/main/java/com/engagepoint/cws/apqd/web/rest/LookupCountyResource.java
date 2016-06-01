package com.engagepoint.cws.apqd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.engagepoint.cws.apqd.domain.LookupCounty;
import com.engagepoint.cws.apqd.repository.LookupCountyRepository;
import com.engagepoint.cws.apqd.repository.search.LookupCountySearchRepository;
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
 * REST controller for managing LookupCounty.
 */
@RestController
@RequestMapping("/api")
public class LookupCountyResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(LookupCountyResource.class);

    @Inject
    private LookupCountyRepository lookupCountyRepository;

    @Inject
    private LookupCountySearchRepository lookupCountySearchRepository;

    /**
     * POST  /lookupCountys -> Create a new lookupCounty.
     */
    @RequestMapping(value = "/lookupCountys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LookupCounty> createLookupCounty(@Valid @RequestBody LookupCounty lookupCounty) throws URISyntaxException {
        LOGGER.debug("REST request to save LookupCounty : {}", lookupCounty);
        if (lookupCounty.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lookupCounty", "idexists", "A new lookupCounty cannot already have an ID")).body(null);
        }
        LookupCounty result = lookupCountyRepository.save(lookupCounty);
        lookupCountySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/lookupCountys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lookupCounty", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lookupCountys -> Updates an existing lookupCounty.
     */
    @RequestMapping(value = "/lookupCountys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LookupCounty> updateLookupCounty(@Valid @RequestBody LookupCounty lookupCounty) throws URISyntaxException {
        LOGGER.debug("REST request to update LookupCounty : {}", lookupCounty);
        if (lookupCounty.getId() == null) {
            return createLookupCounty(lookupCounty);
        }
        LookupCounty result = lookupCountyRepository.save(lookupCounty);
        lookupCountySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lookupCounty", lookupCounty.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lookupCountys -> get all the lookupCountys.
     */
    @RequestMapping(value = "/lookupCountys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LookupCounty> getAllLookupCountys() {
        LOGGER.debug("REST request to get all LookupCountys");
        return lookupCountyRepository.findAll();
            }

    /**
     * GET  /lookupCountys/:id -> get the "id" lookupCounty.
     */
    @RequestMapping(value = "/lookupCountys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LookupCounty> getLookupCounty(@PathVariable Long id) {
        LOGGER.debug("REST request to get LookupCounty : {}", id);
        LookupCounty lookupCounty = lookupCountyRepository.findOne(id);
        return Optional.ofNullable(lookupCounty)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lookupCountys/:id -> delete the "id" lookupCounty.
     */
    @RequestMapping(value = "/lookupCountys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLookupCounty(@PathVariable Long id) {
        LOGGER.debug("REST request to delete LookupCounty : {}", id);
        lookupCountyRepository.delete(id);
        lookupCountySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lookupCounty", id.toString())).build();
    }

    /**
     * SEARCH  /_search/lookupCountys/:query -> search for the lookupCounty corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/lookupCountys/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LookupCounty> searchLookupCountys(@PathVariable String query) {
        LOGGER.debug("REST request to search LookupCountys for query {}", query);
        return StreamSupport
            .stream(lookupCountySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
