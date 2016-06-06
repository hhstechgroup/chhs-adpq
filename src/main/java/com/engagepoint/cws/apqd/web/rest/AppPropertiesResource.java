package com.engagepoint.cws.apqd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.engagepoint.cws.apqd.config.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * @author Oleg.Korneychuk
 * @version 6/4/2016
 */
@RestController
@RequestMapping("/api/app-properties")
public class AppPropertiesResource {
    private final Logger log = LoggerFactory.getLogger(AppPropertiesResource.class);

    @Inject
    private AppProperties appProperties;

    @RequestMapping(value = "/default-address",
        method = RequestMethod.GET,
        produces = MediaType.TEXT_PLAIN_VALUE)
    @Timed
    public String getDefaultAddress() {
        log.debug("REST request to get default-address");
        return appProperties.getDefaultAddress();
    }
}
