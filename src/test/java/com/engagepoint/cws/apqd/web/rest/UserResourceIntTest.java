package com.engagepoint.cws.apqd.web.rest;

import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.MockMailSender;
import com.engagepoint.cws.apqd.config.JHipsterProperties;
import com.engagepoint.cws.apqd.domain.User;
import com.engagepoint.cws.apqd.repository.AuthorityRepository;
import com.engagepoint.cws.apqd.repository.UserRepository;
import com.engagepoint.cws.apqd.service.MailService;
import com.engagepoint.cws.apqd.service.UserService;
import com.engagepoint.cws.apqd.web.rest.dto.ManagedUserDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.inject.Inject;

import static com.engagepoint.cws.apqd.APQDTestUtil.expectUser;
import static com.engagepoint.cws.apqd.APQDTestUtil.newUserAnnaBrown;
import static com.engagepoint.cws.apqd.APQDTestUtil.prepareUser;
import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserResourceIntTest {
    private static final int USER_COUNT = 15;

    /*
     * for MailService
     */

    @Inject
    private JHipsterProperties jHipsterProperties;

    @Inject
    private MessageSource messageSource;

    @Inject
    private SpringTemplateEngine templateEngine;

    /*
     * for UserResource
     */

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private UserService userService;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserMockMvc;

    @Before
    public void setup() {
        MailService mailService = new MailService();
        ReflectionTestUtils.setField(mailService, "jHipsterProperties", jHipsterProperties);
        ReflectionTestUtils.setField(mailService, "javaMailSender", new MockMailSender());
        ReflectionTestUtils.setField(mailService, "messageSource", messageSource);
        ReflectionTestUtils.setField(mailService, "templateEngine", templateEngine);

        UserResource userResource = new UserResource();
        ReflectionTestUtils.setField(userResource, "userRepository", userRepository);
        ReflectionTestUtils.setField(userResource, "mailService", mailService);
        ReflectionTestUtils.setField(userResource, "userService", userService);

        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource)
            .setCustomArgumentResolvers(pageableArgumentResolver).build();
    }

    @Test
    public void testGetExistingUser() throws Exception {
        restUserMockMvc.perform(get("/api/users/admin")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.lastName").value("Administrator"));
    }

    @Test
    public void testGetUnknownUser() throws Exception {
        restUserMockMvc.perform(get("/api/users/unknown")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private void initGetAllUsers() {
        for (int i = 1; i < USER_COUNT; i++) {
            prepareUser(userRepository, passwordEncoder, "user" + i);
        }
    }

    @Test
    @Transactional
    public void testGetAllUsers() throws Exception {
        initGetAllUsers();

        restUserMockMvc.perform(get("/api/users?sort=id,asc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(header().string("X-Total-Count", notNullValue()))
            .andExpect(header().string("X-Total-Count", greaterThanOrEqualTo(String.valueOf(USER_COUNT))))
            .andExpect(header().string(HttpHeaders.LINK, notNullValue()))
            .andExpect(header().string(HttpHeaders.LINK, startsWith("<")));
    }

    @Test()
    @Transactional
    public void testCreateAndUpdateUser() throws Exception {
        User user = newUserAnnaBrown(passwordEncoder, authorityRepository);

        // create user

        ManagedUserDTO managedUserDTO = new ManagedUserDTO(user);
        assertThat(managedUserDTO.getId()).isNull();

        ResultActions resultActions = restUserMockMvc.perform(
            post("/api/users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(
                    managedUserDTO
                )))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(header().string("X-apqdApp-alert", "user-management.created"))
            .andExpect(header().string("X-apqdApp-params", user.getLogin()));

        expectUser(resultActions, user);

        user = userRepository.findAll().iterator().next();

        user.setFirstName("Anishka");
        user.setLastName("Krzhykova");
        user.setSsnLast4Digits("5678");

        // update user

        managedUserDTO = new ManagedUserDTO(user);
        assertThat(managedUserDTO.getId()).isNotNull();

        resultActions = restUserMockMvc.perform(
            put("/api/users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(
                    managedUserDTO
                )))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(header().string("X-apqdApp-alert", "user-management.updated"))
            .andExpect(header().string("X-apqdApp-params", user.getLogin()));

        expectUser(resultActions, user);
    }
}
