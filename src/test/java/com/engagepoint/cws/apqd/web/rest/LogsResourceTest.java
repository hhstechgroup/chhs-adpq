package com.engagepoint.cws.apqd.web.rest;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.web.rest.dto.LoggerDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class LogsResourceTest {

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restResourceMockMvc;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);

        LogsResource logsResource = new LogsResource();

        this.restResourceMockMvc = MockMvcBuilders.standaloneSetup(logsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Test
    public void testGetList() throws Exception {
        restResourceMockMvc.perform(
            get("/api/logs"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].name").value(hasItem("ROOT")))
            .andExpect(jsonPath("$.[*].name").value(hasItem("com.engagepoint.cws.apqd.Application")))
            .andExpect(jsonPath("$.[*].name").value(hasItem("com.engagepoint.cws.apqd.config.WebsocketConfiguration")))
            .andExpect(jsonPath("$.[*].name").value(hasItem("com.engagepoint.cws.apqd.service.MailService")))
            .andExpect(jsonPath("$.[*].name").value(hasItem("com.engagepoint.cws.apqd.service.UserService")))
            .andExpect(jsonPath("$.[*].level").value(hasItem("WARN")));
    }

    @Test
    public void testChangeLevel() throws Exception {
        LoggerDTO loggerDTO = new LoggerDTO();
        loggerDTO.setName("ROOT");
        loggerDTO.setLevel("INFO");

        restResourceMockMvc.perform(
            put("/api/logs").contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(loggerDTO)))
            .andExpect(status().isNoContent());

        restResourceMockMvc.perform(
            get("/api/logs"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].level").value(hasItem("INFO")));
    }
}
