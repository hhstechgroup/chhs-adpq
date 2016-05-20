package com.engagepoint.cws.apqd.config;

import com.engagepoint.cws.apqd.service.usps.UspsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

/**
 * Created by dmytro.palczewski on 4/7/2016.
 */
@Configuration
public class UspsConfiguration {

    @Inject
    private UspsProperties uspsProperties;

    @Bean
    public UspsService uspsService(){
        UspsService uspsService = new UspsService();
        uspsService.setUserId(uspsProperties.getUserId());
        uspsService.setServerUrl(uspsProperties.getServerUrl());
        uspsService.setTimeout(uspsProperties.getTimeout());
        return uspsService;
    }
}
