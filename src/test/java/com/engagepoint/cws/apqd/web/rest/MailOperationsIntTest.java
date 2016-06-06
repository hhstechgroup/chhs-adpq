package com.engagepoint.cws.apqd.web.rest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.MockMailSender;
import com.engagepoint.cws.apqd.config.JHipsterProperties;
import com.engagepoint.cws.apqd.domain.User;
import com.engagepoint.cws.apqd.repository.AuthorityRepository;
import com.engagepoint.cws.apqd.repository.UserRepository;
import com.engagepoint.cws.apqd.security.AuthoritiesConstants;
import com.engagepoint.cws.apqd.service.MailService;
import com.engagepoint.cws.apqd.service.UserService;
import com.engagepoint.cws.apqd.service.util.RandomUtil;
import com.engagepoint.cws.apqd.web.rest.dto.KeyAndPasswordDTO;
import com.engagepoint.cws.apqd.web.rest.dto.UserDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.engagepoint.cws.apqd.APQDTestUtil.*;
import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MailOperationsIntTest {
    /*
     * for MailService
     */

    @Inject
    private JHipsterProperties jHipsterProperties;

    private MockMailSender mockMailSender;

    private MockMailSender failingMockMailSender;

    @Inject
    private MessageSource messageSource;

    @Inject
    private SpringTemplateEngine templateEngine;

    private MailService mailService;

    /*
     * for UserResource
     */

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserService userService;

    @Inject
    private AuthorityRepository authorityRepository;

    //

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMockMvc;

    private Appender<ILoggingEvent> mockLogAppender;

    @PostConstruct
    public void setup() {
        mockLogAppender = mock(Appender.class);
        when(mockLogAppender.getName()).thenReturn("MOCK");

        mockMailSender = new MockMailSender();
        failingMockMailSender = new MockMailSender(true);

        mailService = new MailService();
        ReflectionTestUtils.setField(mailService, "jHipsterProperties", jHipsterProperties);
        ReflectionTestUtils.setField(mailService, "messageSource", messageSource);
        ReflectionTestUtils.setField(mailService, "templateEngine", templateEngine);

        UserResource userResource = new UserResource();
        ReflectionTestUtils.setField(userResource, "userRepository", userRepository);
        ReflectionTestUtils.setField(userResource, "mailService", mailService);
        ReflectionTestUtils.setField(userResource, "userService", userService);

        AccountResource accountResource = new AccountResource();
        ReflectionTestUtils.setField(accountResource, "userRepository", userRepository);
        ReflectionTestUtils.setField(accountResource, "userService", userService);
        ReflectionTestUtils.setField(accountResource, "mailService", mailService);

        this.restMockMvc = MockMvcBuilders.standaloneSetup(userResource, accountResource)
            .setCustomArgumentResolvers(pageableArgumentResolver).build();
    }

    @Before
    public void before() {
        Logger logger = (Logger) LoggerFactory.getLogger(MailService.class);
        logger.addAppender(mockLogAppender);

        ReflectionTestUtils.setField(mailService, "javaMailSender", mockMailSender);
    }

    @After
    public void after() {
        Logger logger = (Logger) LoggerFactory.getLogger(MailService.class);
        logger.detachAppender(mockLogAppender);
    }

    /**
     * Process email with a thing like this:
     *      <a href="http://localhost:80/#/reset/finish?key=27283578379851735202">newuser</a>
     *
     * @return the value of the "key" parameter
     */
    private String extractKeyParameterFromLastEmail() throws Exception {
        // assume the asserts for the message are already executed
        String content = mockMailSender.getFutureMimeMessage().get().getContent().toString();
        Matcher matcher = Pattern.compile("\"http[^\"]+key=(\\d+)\"").matcher(content);
        return matcher.find() ? matcher.group(1) : "";
    }

    @Test
    @Transactional
    public void testMailSendFailed() throws Exception {
        ReflectionTestUtils.setField(mailService, "javaMailSender", failingMockMailSender);

        User user = newUserAnnaBrown(passwordEncoder, authorityRepository);

        performCreateUser(restMockMvc, user).andExpect(status().isCreated());

        verify(mockLogAppender).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
            @Override
            public boolean matches(final Object argument) {
                LoggingEvent loggingEvent = (LoggingEvent) argument;
                return loggingEvent.getLevel().equals(Level.WARN) && loggingEvent.getFormattedMessage().startsWith(
                    String.format("E-mail could not be sent to user '%s'", user.getEmail()));
            }
        }));
    }

    @Test
    @Transactional
    public void testCreateUser() throws Exception {
        User user = newUserAnnaBrown(passwordEncoder, authorityRepository);

        performCreateUser(restMockMvc, user).andExpect(status().isCreated());

        assertUserEmail(jHipsterProperties, messageSource, templateEngine, mockMailSender,
            user, "email.activation.title", "creationEmail");

        // also test AccountResource.getAccount

        setCurrentUser(user);

        restMockMvc.perform(get("/api/account")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.login").value(user.getLogin()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.authorities").value(AuthoritiesConstants.USER));
    }

    @Test
    @Transactional
    public void testRegisterValid() throws Exception {
        User user = newUserAnnaBrown(passwordEncoder, authorityRepository);
        UserDTO userDTO = new UserDTO(user, user.getPassword());

        // registerAccount

        restMockMvc.perform(
            post("/api/register")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(
                    userDTO
                )))
            .andExpect(status().isCreated());

        Optional<User> testUser = userRepository.findOneByLogin(user.getLogin());
        assertThat(testUser).isNotNull();
        assertThat(testUser.isPresent()).isTrue();

        assertUserEmail(jHipsterProperties, messageSource, templateEngine, mockMailSender,
            user, "email.activation.title", "activationEmail");

        // activateAccount

        restMockMvc.perform(
            get(String.format("/api/activate?key=%s", extractKeyParameterFromLastEmail())))
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void testRequestPasswordResetSuccess() throws Exception {
        User user = newUserAnnaBrown(passwordEncoder, authorityRepository);
        userRepository.saveAndFlush(user);

        // requestPasswordReset

        restMockMvc.perform(
            post("/api/account/reset_password/init")
                .content(
                    user.getEmail()
                ))
            .andExpect(status().isOk());

        assertUserEmail(jHipsterProperties, messageSource, templateEngine, mockMailSender,
            user, "email.reset.title", "passwordResetEmail");

        // finishPasswordReset

        KeyAndPasswordDTO keyAndPasswordDTO = new KeyAndPasswordDTO();
        keyAndPasswordDTO.setKey(extractKeyParameterFromLastEmail());
        keyAndPasswordDTO.setNewPassword(RandomUtil.generatePassword());

        restMockMvc.perform(
            post("/api/account/reset_password/finish")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(
                    keyAndPasswordDTO
                )))
            .andExpect(status().isOk());
    }
}
