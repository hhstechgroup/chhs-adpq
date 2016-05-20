package com.engagepoint.cws.apqd.web.rest;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.service.usps.UspsAddress;
import com.engagepoint.cws.apqd.service.usps.UspsResponseType;
import com.engagepoint.cws.apqd.service.usps.UspsService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dmytro.palczewski on 2/12/2016.
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UspsResourceTest {

    @Inject
    private UspsService uspsService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restUspsMockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UspsResource uspsResource = new UspsResource();
        ReflectionTestUtils.setField(uspsResource, "uspsService", uspsService);
        this.restUspsMockMvc =
            MockMvcBuilders.standaloneSetup(uspsResource).
            setMessageConverters(jacksonMessageConverter).build();
    }

    @Test
    public void verifyAddress() throws Exception {

        UspsAddress request = new UspsAddress();
        request.setStreetAddress("3901 Calverton blvd");
        request.setApartmentOrSuite("110");
        request.setCity("Calverton");
        request.setState("MD");
        request.setZip("20705");
        request.setZip4("");

        restUspsMockMvc.perform(put("/usps/verify-address")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.responseType").value(UspsResponseType.SUCCESS.toString()))
            .andExpect(jsonPath("$.address.zip").value("20705"));
    }
}
