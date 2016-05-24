package com.engagepoint.cws.apqd.web.rest.hhsapi;

import com.codahale.metrics.annotation.Timed;
import com.engagepoint.cws.apqd.service.hhsapi.HHSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by dmitry.rudenko on 5/20/2016.
 */
@RestController
@RequestMapping("/api")
public class HHSResource {
    @Autowired
    private HHSService hhsService;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/hhs/findFosterFamilyAgencies",
        method = RequestMethod.POST, produces = "application/json")
    @Timed
    @ResponseBody
    public String findFosterFamilyAgencies(@RequestBody Box box) throws IOException, URISyntaxException {
        return hhsService.findFosterFamilyAgencies(box.getNorthwest().getLatitude(), box.getNorthwest().getLongitude(),
            box.getSoutheast().getLatitude(), box.getSoutheast().getLongitude());
    }

    @RequestMapping(value = "/hhs/fosterFamilyAgencies.json",
        method = RequestMethod.GET, produces = "application/json")
    public String findFosterFamilyAgencies() throws IOException, URISyntaxException {
        String queryString = request.getQueryString();
        return hhsService.findFosterFamilyAgencies(queryString);
    }
}
