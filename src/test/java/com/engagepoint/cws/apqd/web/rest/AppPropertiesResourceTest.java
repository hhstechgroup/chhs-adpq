package com.engagepoint.cws.apqd.web.rest;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.config.AppProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Oleg.Korneychuk
 * @version 6/4/2016
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AppPropertiesResourceTest {

    @Inject
    private AppProperties appProperties;

    private MockMvc restAppPropertiesResourceMockMvc;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AppPropertiesResource AppPropertiesResource = new AppPropertiesResource();
        ReflectionTestUtils.setField(AppPropertiesResource, "appProperties", appProperties);

        restAppPropertiesResourceMockMvc = MockMvcBuilders.standaloneSetup(AppPropertiesResource).build();
    }

    @Test
    public void testGetDefaultAddress() throws Exception {
        restAppPropertiesResourceMockMvc.perform(get("/api/app-properties/default-address"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.TEXT_PLAIN))
            .andExpect(content().string("test defaultAddres"));
    }
}
