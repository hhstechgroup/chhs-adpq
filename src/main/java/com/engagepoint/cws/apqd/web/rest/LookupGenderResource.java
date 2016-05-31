package com.engagepoint.cws.apqd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.engagepoint.cws.apqd.domain.LookupGender;
import com.engagepoint.cws.apqd.repository.LookupGenderRepository;
import com.engagepoint.cws.apqd.repository.search.LookupGenderSearchRepository;
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
 * REST controller for managing LookupGender.
 */
@RestController
@RequestMapping("/api")
public class LookupGenderResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(LookupGenderResource.class);

    @Inject
    private LookupGenderRepository lookupGenderRepository;

    @Inject
    private LookupGenderSearchRepository lookupGenderSearchRepository;

    /**
     * POST  /lookupGenders -> Create a new lookupGender.
     */
    @RequestMapping(value = "/lookupGenders",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LookupGender> createLookupGender(@Valid @RequestBody LookupGender lookupGender) throws URISyntaxException {
        LOGGER.debug("REST request to save LookupGender : {}", lookupGender);
        if (lookupGender.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lookupGender", "idexists", "A new lookupGender cannot already have an ID")).body(null);
        }
        LookupGender result = lookupGenderRepository.save(lookupGender);
        lookupGenderSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/lookupGenders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lookupGender", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lookupGenders -> Updates an existing lookupGender.
     */
    @RequestMapping(value = "/lookupGenders",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LookupGender> updateLookupGender(@Valid @RequestBody LookupGender lookupGender) throws URISyntaxException {
        LOGGER.debug("REST request to update LookupGender : {}", lookupGender);
        if (lookupGender.getId() == null) {
            return createLookupGender(lookupGender);
        }
        LookupGender result = lookupGenderRepository.save(lookupGender);
        lookupGenderSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lookupGender", lookupGender.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lookupGenders -> get all the lookupGenders.
     */
    @RequestMapping(value = "/lookupGenders",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LookupGender> getAllLookupGenders() {
        LOGGER.debug("REST request to get all LookupGenders");
        return lookupGenderRepository.findAll();
            }

    /**
     * GET  /lookupGenders/:id -> get the "id" lookupGender.
     */
    @RequestMapping(value = "/lookupGenders/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LookupGender> getLookupGender(@PathVariable Long id) {
        LOGGER.debug("REST request to get LookupGender : {}", id);
        LookupGender lookupGender = lookupGenderRepository.findOne(id);
        return Optional.ofNullable(lookupGender)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lookupGenders/:id -> delete the "id" lookupGender.
     */
    @RequestMapping(value = "/lookupGenders/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLookupGender(@PathVariable Long id) {
        LOGGER.debug("REST request to delete LookupGender : {}", id);
        lookupGenderRepository.delete(id);
        lookupGenderSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lookupGender", id.toString())).build();
    }

    /**
     * SEARCH  /_search/lookupGenders/:query -> search for the lookupGender corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/lookupGenders/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<LookupGender> searchLookupGenders(@PathVariable String query) {
        LOGGER.debug("REST request to search LookupGenders for query {}", query);
        return StreamSupport
            .stream(lookupGenderSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
