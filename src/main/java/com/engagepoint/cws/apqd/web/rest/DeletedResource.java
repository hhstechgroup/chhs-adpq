package com.engagepoint.cws.apqd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.engagepoint.cws.apqd.domain.Deleted;
import com.engagepoint.cws.apqd.repository.DeletedRepository;
import com.engagepoint.cws.apqd.repository.search.DeletedSearchRepository;
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
 * REST controller for managing Deleted.
 */
@RestController
@RequestMapping("/api")
public class DeletedResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeletedResource.class);

    @Inject
    private DeletedRepository deletedRepository;

    @Inject
    private DeletedSearchRepository deletedSearchRepository;

    /**
     * POST  /deleteds -> Create a new deleted.
     */
    @RequestMapping(value = "/deleteds",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Deleted> createDeleted(@RequestBody Deleted deleted) throws URISyntaxException {
        LOGGER.debug("REST request to save Deleted : {}", deleted);
        if (deleted.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("deleted", "idexists", "A new deleted cannot already have an ID")).body(null);
        }
        Deleted result = deletedRepository.save(deleted);
        deletedSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/deleteds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("deleted", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /deleteds -> Updates an existing deleted.
     */
    @RequestMapping(value = "/deleteds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Deleted> updateDeleted(@RequestBody Deleted deleted) throws URISyntaxException {
        LOGGER.debug("REST request to update Deleted : {}", deleted);
        if (deleted.getId() == null) {
            return createDeleted(deleted);
        }
        Deleted result = deletedRepository.save(deleted);
        deletedSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("deleted", deleted.getId().toString()))
            .body(result);
    }

    /**
     * GET  /deleteds -> get all the deleteds.
     */
    @RequestMapping(value = "/deleteds",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Deleted> getAllDeleteds(@RequestParam(required = false) String filter) {
        if ("mailbox-is-null".equals(filter)) {
            LOGGER.debug("REST request to get all Deleteds where mailBox is null");
            return StreamSupport
                .stream(deletedRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        }
        LOGGER.debug("REST request to get all Deleteds");
        return deletedRepository.findAll();
            }

    /**
     * GET  /deleteds/:id -> get the "id" deleted.
     */
    @RequestMapping(value = "/deleteds/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Deleted> getDeleted(@PathVariable Long id) {
        LOGGER.debug("REST request to get Deleted : {}", id);
        Deleted deleted = deletedRepository.findOne(id);
        return Optional.ofNullable(deleted)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /deleteds/:id -> delete the "id" deleted.
     */
    @RequestMapping(value = "/deleteds/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDeleted(@PathVariable Long id) {
        LOGGER.debug("REST request to delete Deleted : {}", id);
        deletedRepository.delete(id);
        deletedSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("deleted", id.toString())).build();
    }

    /**
     * SEARCH  /_search/deleteds/:query -> search for the deleted corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/deleteds/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Deleted> searchDeleteds(@PathVariable String query) {
        LOGGER.debug("REST request to search Deleteds for query {}", query);
        return StreamSupport
            .stream(deletedSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
