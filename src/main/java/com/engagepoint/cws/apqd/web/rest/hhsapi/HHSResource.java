package com.engagepoint.cws.apqd.web.rest.hhsapi;

import com.codahale.metrics.annotation.Timed;
import com.engagepoint.cws.apqd.service.hhsapi.HHSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by dmitry.rudenko on 5/20/2016.
 */
@RestController
@RequestMapping("/api")
public class HHSResource {
    @Autowired
    private HHSService hhsService;

    @RequestMapping(value = "/hhs/findFosterFamilyAgencies",
        method = RequestMethod.POST)
    @Timed
    @ResponseBody
    public String findFosterFamilyAgencies(@RequestBody Box box) throws Exception {
        return hhsService.findFosterFamilyAgencies(box.getNorthwest().getLatitude(), box.getNorthwest().getLongitude(),
            box.getSoutheast().getLatitude(), box.getSoutheast().getLongitude());
    }
}
