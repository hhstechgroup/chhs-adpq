package com.engagepoint.cws.apqd.web.rest;

import com.engagepoint.cws.apqd.service.usps.UspsAddress;
import com.engagepoint.cws.apqd.service.usps.UspsResponse;
import com.engagepoint.cws.apqd.service.usps.UspsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

/**
 * Created by dmytro.palczewski on 2/12/2016.
 */
@RestController
@RequestMapping("/usps")
public class UspsResource {

    private final Logger log = LoggerFactory.getLogger(UspsResource.class);

    @Inject
    private UspsService uspsService;

    @RequestMapping(value = "/verify-address",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public UspsResponse verifyAddress(@RequestBody UspsAddress inputAddress){

        log.debug("REST request verify address : {}", inputAddress);
        UspsResponse uspsResponse = uspsService.verifyAddress(inputAddress);
        return uspsResponse;
    }
}
