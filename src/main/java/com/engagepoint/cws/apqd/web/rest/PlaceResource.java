package com.engagepoint.cws.apqd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.engagepoint.cws.apqd.domain.Place;
import com.engagepoint.cws.apqd.repository.PlaceRepository;
import com.engagepoint.cws.apqd.repository.search.PlaceSearchRepository;
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
 * REST controller for managing Place.
 */
@RestController
@RequestMapping("/api")
public class PlaceResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlaceResource.class);

    @Inject
    private PlaceRepository placeRepository;

    @Inject
    private PlaceSearchRepository placeSearchRepository;

    /**
     * POST  /places -> Create a new place.
     */
    @RequestMapping(value = "/places",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Place> createPlace(@Valid @RequestBody Place place) throws URISyntaxException {
        LOGGER.debug("REST request to save Place : {}", place);
        if (place.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("place", "idexists", "A new place cannot already have an ID")).body(null);
        }
        Place result = placeRepository.save(place);
        placeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/places/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("place", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /places -> Updates an existing place.
     */
    @RequestMapping(value = "/places",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Place> updatePlace(@Valid @RequestBody Place place) throws URISyntaxException {
        LOGGER.debug("REST request to update Place : {}", place);
        if (place.getId() == null) {
            return createPlace(place);
        }
        Place result = placeRepository.save(place);
        placeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("place", place.getId().toString()))
            .body(result);
    }

    /**
     * GET  /places -> get all the places.
     */
    @RequestMapping(value = "/places",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Place> getAllPlaces() {
        LOGGER.debug("REST request to get all Places");
        return placeRepository.findAll();
            }

    /**
     * GET  /places/:id -> get the "id" place.
     */
    @RequestMapping(value = "/places/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Place> getPlace(@PathVariable Long id) {
        LOGGER.debug("REST request to get Place : {}", id);
        Place place = placeRepository.findOne(id);
        return Optional.ofNullable(place)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /places/:id -> delete the "id" place.
     */
    @RequestMapping(value = "/places/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
        LOGGER.debug("REST request to delete Place : {}", id);
        placeRepository.delete(id);
        placeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("place", id.toString())).build();
    }

    /**
     * SEARCH  /_search/places/:query -> search for the place corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/places/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Place> searchPlaces(@PathVariable String query) {
        LOGGER.debug("REST request to search Places for query {}", query);
        return StreamSupport
            .stream(placeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
