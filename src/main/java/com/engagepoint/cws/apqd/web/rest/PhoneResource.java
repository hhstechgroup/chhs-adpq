package com.engagepoint.cws.apqd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.engagepoint.cws.apqd.domain.Phone;
import com.engagepoint.cws.apqd.repository.PhoneRepository;
import com.engagepoint.cws.apqd.repository.search.PhoneSearchRepository;
import com.engagepoint.cws.apqd.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
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
 * REST controller for managing Phone.
 */
@RestController
@RequestMapping("/api")
public class PhoneResource {

    private final Logger log = LoggerFactory.getLogger(PhoneResource.class);
        
    @Inject
    private PhoneRepository phoneRepository;
    
    @Inject
    private PhoneSearchRepository phoneSearchRepository;
    
    /**
     * POST  /phones -> Create a new phone.
     */
    @RequestMapping(value = "/phones",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Phone> createPhone(@Valid @RequestBody Phone phone) throws URISyntaxException {
        log.debug("REST request to save Phone : {}", phone);
        if (phone.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("phone", "idexists", "A new phone cannot already have an ID")).body(null);
        }
        Phone result = phoneRepository.save(phone);
        phoneSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/phones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("phone", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /phones -> Updates an existing phone.
     */
    @RequestMapping(value = "/phones",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Phone> updatePhone(@Valid @RequestBody Phone phone) throws URISyntaxException {
        log.debug("REST request to update Phone : {}", phone);
        if (phone.getId() == null) {
            return createPhone(phone);
        }
        Phone result = phoneRepository.save(phone);
        phoneSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("phone", phone.getId().toString()))
            .body(result);
    }

    /**
     * GET  /phones -> get all the phones.
     */
    @RequestMapping(value = "/phones",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Phone> getAllPhones() {
        log.debug("REST request to get all Phones");
        return phoneRepository.findAll();
            }

    /**
     * GET  /phones/:id -> get the "id" phone.
     */
    @RequestMapping(value = "/phones/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Phone> getPhone(@PathVariable Long id) {
        log.debug("REST request to get Phone : {}", id);
        Phone phone = phoneRepository.findOne(id);
        return Optional.ofNullable(phone)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /phones/:id -> delete the "id" phone.
     */
    @RequestMapping(value = "/phones/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePhone(@PathVariable Long id) {
        log.debug("REST request to delete Phone : {}", id);
        phoneRepository.delete(id);
        phoneSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("phone", id.toString())).build();
    }

    /**
     * SEARCH  /_search/phones/:query -> search for the phone corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/phones/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Phone> searchPhones(@PathVariable String query) {
        log.debug("REST request to search Phones for query {}", query);
        return StreamSupport
            .stream(phoneSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
