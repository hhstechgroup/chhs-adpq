package com.engagepoint.cws.apqd.web.rest;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;
import com.engagepoint.cws.apqd.Application;
import com.engagepoint.cws.apqd.MockMailSender;
import com.engagepoint.cws.apqd.config.JHipsterProperties;
import com.engagepoint.cws.apqd.domain.User;
import com.engagepoint.cws.apqd.repository.AuthorityRepository;
import com.engagepoint.cws.apqd.repository.UserRepository;
import com.engagepoint.cws.apqd.service.MailService;
import com.engagepoint.cws.apqd.service.UserService;
import com.engagepoint.cws.apqd.service.util.RandomUtil;
import com.engagepoint.cws.apqd.web.rest.dto.KeyAndPasswordDTO;
import com.engagepoint.cws.apqd.web.rest.dto.ManagedUserDTO;
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
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.mail.Address;
import javax.mail.internet.MimeMessage;

import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.Future;
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

    private Appender mockLogAppender;

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
    public void before () {
        Logger logger = (Logger) LoggerFactory.getLogger(MailService.class);
        logger.addAppender(mockLogAppender);

        ReflectionTestUtils.setField(mailService, "javaMailSender", mockMailSender);
    }

    @After
    public void after() {
        Logger logger = (Logger) LoggerFactory.getLogger(MailService.class);
        logger.detachAppender(mockLogAppender);
    }

    private void assertThatMessageFromIs(MimeMessage mimeMessage, String email) throws Exception {
        Address[] senders = mimeMessage.getFrom();
        assertThat(senders).isNotNull();
        assertThat(senders.length).isGreaterThan(0);
        assertThat(senders[0]).isNotNull();
        assertThat(senders[0].toString()).isEqualTo(email);
    }

    private void assertThatMessageRecipientIs(MimeMessage mimeMessage, String email) throws Exception {
        Address[] recipients = mimeMessage.getAllRecipients();
        assertThat(recipients).isNotNull();
        assertThat(recipients.length).isGreaterThan(0);
        assertThat(recipients[0]).isNotNull();
        assertThat(recipients[0].toString()).isEqualTo(email);
    }

    private String getUserEmailSubject(User user, String subjectKey) {
        return messageSource.getMessage(subjectKey, null, Locale.forLanguageTag(user.getLangKey()));
    }

    private String getUserEmailContent(User user, String contentTemplateName) {
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", "http://localhost:80/");

        return templateEngine.process(contentTemplateName, context);
    }

    private void assertUserEmail(User user, String subjectKey, String contentTemplateName) throws Exception {
        Future<MimeMessage> futureMimeMessage = mockMailSender.getFutureMimeMessage();
        assertThat(futureMimeMessage).isNotNull();
        assertThat(futureMimeMessage.isDone()).isTrue();

        MimeMessage mimeMessage = futureMimeMessage.get();
        assertThat(mimeMessage).isNotNull();

        assertThatMessageFromIs(mimeMessage, jHipsterProperties.getMail().getFrom());
        assertThatMessageRecipientIs(mimeMessage, user.getEmail());

        assertThat(mimeMessage.getSubject()).isNotNull();
        assertThat(mimeMessage.getSubject()).isEqualTo(getUserEmailSubject(user, subjectKey));

        assertThat(mimeMessage.getContent()).isNotNull();
        assertThat(
            cutQuotedUrls(mimeMessage.getContent().toString())
        ).isEqualTo(
            cutQuotedUrls(getUserEmailContent(user, contentTemplateName))
        );
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

    @Test()
    @Transactional
    public void testMailSendFailed() throws Exception {
        ReflectionTestUtils.setField(mailService, "javaMailSender", failingMockMailSender);

        User user = newUserAnnaBrown(passwordEncoder, authorityRepository);

        ManagedUserDTO managedUserDTO = new ManagedUserDTO(user);
        assertThat(managedUserDTO.getId()).isNull();

        restMockMvc.perform(
            post("/api/users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(
                    managedUserDTO
                )))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(mockLogAppender).doAppend(argThat(new ArgumentMatcher() {
            @Override
            public boolean matches(final Object argument) {
                LoggingEvent loggingEvent = (LoggingEvent) argument;
                return loggingEvent.getLevel().equals(Level.WARN) && loggingEvent.getFormattedMessage().startsWith(
                    String.format("E-mail could not be sent to user '%s'", user.getEmail()));
            }
        }));
    }

    @Test()
    @Transactional
    public void testCreateUser() throws Exception {
        User user = newUserAnnaBrown(passwordEncoder, authorityRepository);

        ManagedUserDTO managedUserDTO = new ManagedUserDTO(user);
        assertThat(managedUserDTO.getId()).isNull();

        restMockMvc.perform(
            post("/api/users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(
                    managedUserDTO
                )))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        assertUserEmail(user, "email.activation.title", "creationEmail");
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

        assertUserEmail(user, "email.activation.title", "activationEmail");

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

        assertUserEmail(user, "email.reset.title", "passwordResetEmail");

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
