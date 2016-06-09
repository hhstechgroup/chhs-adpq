package com.engagepoint.cws.apqd;

import com.engagepoint.cws.apqd.config.JHipsterProperties;
import com.engagepoint.cws.apqd.domain.Draft;
import com.engagepoint.cws.apqd.domain.Inbox;
import com.engagepoint.cws.apqd.domain.LookupGender;
import com.engagepoint.cws.apqd.domain.MailBox;
import com.engagepoint.cws.apqd.domain.Message;
import com.engagepoint.cws.apqd.domain.Outbox;
import com.engagepoint.cws.apqd.domain.User;
import com.engagepoint.cws.apqd.repository.AuthorityRepository;
import com.engagepoint.cws.apqd.repository.DraftRepository;
import com.engagepoint.cws.apqd.repository.InboxRepository;
import com.engagepoint.cws.apqd.repository.MailBoxRepository;
import com.engagepoint.cws.apqd.repository.MessageRepository;
import com.engagepoint.cws.apqd.repository.OutboxRepository;
import com.engagepoint.cws.apqd.repository.UserRepository;
import com.engagepoint.cws.apqd.security.AuthoritiesConstants;
import com.engagepoint.cws.apqd.service.util.RandomUtil;
import com.engagepoint.cws.apqd.web.rest.TestUtil;
import com.engagepoint.cws.apqd.web.rest.dto.ContactDTO;
import com.engagepoint.cws.apqd.web.rest.dto.ManagedUserDTO;
import org.assertj.core.api.StrictAssertions;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.Address;
import javax.mail.internet.MimeMessage;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Future;

import static com.engagepoint.cws.apqd.web.rest.util.ContactUtil.extractRoleDescription;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public final class APQDTestUtil {
    private static final LookupGender GENDER = new LookupGender();

    static {
        GENDER.setId(1L);
    }

    /**
     * Use to avoid assertion errors while comparing generated html.
     * For example, this will convert string:
     *      <a href="http://localhost:80/#/reset/finish?key=88156968137982486177">newuser</a>
     * to:
     *      <a href="">newuser</a>
     *
     * @param content string
     * @return string
     */
    public static String cutQuotedUrls(String content) {
        return content.replaceAll("\"http[^\"]+\"", "\"\"");
    }

    /*
     * User-related
     */

    private static org.springframework.security.core.userdetails.User createSpringSecurityUser(User user) {
        final Set<GrantedAuthority> authorities = new HashSet<>();
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), authorities);
    }

    /**
     * Usage:
     *      TestUtil.setCurrentUser(user);
     *      String login = SecurityUtils.getCurrentUserLogin();
     *      assertThat(login).isEqualTo(user.getLogin());
     *
     * @param user User
     */
    public static void setCurrentUser(final User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
            createSpringSecurityUser(user), user.getPassword());
        // authentication set Authenticated true

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    public static User prepareUser(UserRepository userRepository, PasswordEncoder passwordEncoder, String login) {
        User user = new User();
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(RandomUtil.generatePassword()));

        return userRepository == null ? user : userRepository.saveAndFlush(user);
    }

    public static void addUserRole(AuthorityRepository authorityRepository, User user, String role) {
        if (user.getAuthorities() == null) {
            user.setAuthorities(new HashSet<>());
        }
        user.getAuthorities().add(authorityRepository.findOne(role));
    }

    public static void setMailBox(UserRepository userRepository, User user, MailBox mailBox) {
        user.setMailBox(mailBox);
        userRepository.saveAndFlush(user);
    }

    public static User newUserAnnaBrown(PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        User user = prepareUser(null, passwordEncoder, "newuseranna");

        user.setLangKey("en");
        user.setEmail("newuseranna@company.com");
        user.setFirstName("Anna");
        user.setLastName("Brown");
        user.setSsnLast4Digits("4321");
        user.setActivated(true);
        user.setCaseNumber("S123");
        user.setBirthDate(LocalDate.ofEpochDay(0L));
        user.setGender(GENDER);
        user.setPhoneNumber("111-111-1111");

        addUserRole(authorityRepository, user, AuthoritiesConstants.USER);

        return user;
    }

    public static User newUserJohnWhite(PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        User user = prepareUser(null, passwordEncoder, "newuserjohn");

        user.setLangKey("en");
        user.setEmail("newuserjohn@company.com");
        user.setFirstName("John");
        user.setLastName("White");
        user.setSsnLast4Digits("5432");
        user.setActivated(true);
        user.setCaseNumber("S234");
        user.setBirthDate(LocalDate.ofEpochDay(0L));
        user.setPhoneNumber("222-222-2222");

        addUserRole(authorityRepository, user, AuthoritiesConstants.USER);

        return user;
    }

    public static ResultActions performCreateUser(MockMvc restUserMockMvc, User user) throws Exception {
        ManagedUserDTO managedUserDTO = new ManagedUserDTO(user);
        StrictAssertions.assertThat(managedUserDTO.getId()).isNull();

        return restUserMockMvc.perform(
            post("/api/users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(
                    managedUserDTO
                )));
    }

    public static ResultActions performUpdateUser(MockMvc restUserMockMvc, User user) throws Exception {
        ManagedUserDTO managedUserDTO = new ManagedUserDTO(user);
        assertThat(managedUserDTO.getId()).isNotNull();

        return restUserMockMvc.perform(
            put("/api/users")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(
                    managedUserDTO
                )));
    }

    public static void expectUser(ResultActions resultActions, User user) throws Exception {
        resultActions
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.login").value(user.getLogin()))
            .andExpect(jsonPath("$.email").value(user.getEmail()))
            .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
            .andExpect(jsonPath("$.lastName").value(user.getLastName()))
            .andExpect(jsonPath("$.ssnLast4Digits").value(user.getSsnLast4Digits()));
    }

    public static void assertUser(User actual, User expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getLogin()).isEqualTo(expected.getLogin());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        assertThat(actual.getSsnLast4Digits()).isEqualTo(expected.getSsnLast4Digits());
        assertThat(actual.getPhoneNumber()).isEqualTo(expected.getPhoneNumber());
        assertThat(actual.getCaseNumber()).isEqualTo(expected.getCaseNumber());
    }

    private static void expectHasContact(ResultActions resultActions, ContactDTO contactDTO) throws Exception {
        resultActions
            .andExpect(jsonPath("$.[*].login").value(hasItem(contactDTO.getLogin())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(contactDTO.getFirstName())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(contactDTO.getLastName())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(contactDTO.getPhone())))
            .andExpect(jsonPath("$.[*].roleDescription").value(hasItem(contactDTO.getRoleDescription())));
    }

    public static void expectHasContact1(ResultActions resultActions, User contact) throws Exception {
        expectHasContact(resultActions, new ContactDTO(contact));
    }

    public static void expectHasContact2(ResultActions resultActions, User contact) throws Exception {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setLogin(contact.getLogin());
        contactDTO.setFirstName(contact.getFirstName());
        contactDTO.setLastName(contact.getLastName());
        contactDTO.setPhone(contact.getPhoneNumber());
        contactDTO.setPlace(contact.getPlace());
        contactDTO.setRoleDescription(extractRoleDescription(contact));

        expectHasContact(resultActions, contactDTO);
    }

    /*
     * Inbox-related
     */

    public static Inbox prepareInbox(InboxRepository inboxRepository) {
        return inboxRepository.saveAndFlush(new Inbox());
    }

    public static Inbox setMessage(InboxRepository inboxRepository, Inbox inbox, Message message) {
        Set<Message> messages = new HashSet<>();
        messages.add(message);
        inbox.setMessages(messages);
        return inboxRepository.saveAndFlush(inbox);
    }

    /*
     * Outbox-related
     */

    public static Outbox prepareOutbox(OutboxRepository outboxRepository) {
        return outboxRepository.saveAndFlush(new Outbox());
    }

    public static Outbox setMessage(OutboxRepository outboxRepository, Outbox outbox, Message message) {
        Set<Message> messages = new HashSet<>();
        messages.add(message);
        outbox.setMessages(messages);
        return outboxRepository.saveAndFlush(outbox);
    }

    /*
     * Draft-related
     */

    public static Draft prepareDraft(DraftRepository draftRepository) {
        return draftRepository.saveAndFlush(new Draft());
    }

    public static Draft setMessage(DraftRepository draftRepository, Draft draft, Message message) {
        Set<Message> messages = new HashSet<>();
        messages.add(message);
        draft.setMessages(messages);
        return draftRepository.saveAndFlush(draft);
    }

    /*
     * MailBox-related
     */

    public static MailBox prepareMailBox(MailBoxRepository mailBoxRepository, Inbox inbox, Outbox outbox, Draft draft, User user) {
        MailBox mailBox = new MailBox();
        mailBox.setInbox(inbox);
        mailBox.setOutbox(outbox);
        mailBox.setDraft(draft);
        mailBox.setUser(user);
        return mailBoxRepository.saveAndFlush(mailBox);
    }

    public static MailBox prepareMailBox(MailBoxRepository mailBoxRepository, InboxRepository inboxRepository,
                                         OutboxRepository outboxRepository, DraftRepository draftRepository) {
        return prepareMailBox(mailBoxRepository, prepareInbox(inboxRepository), prepareOutbox(outboxRepository),
            prepareDraft(draftRepository), null);
    }

    /*
     * Message-related
     */

    public static Message prepareMessage(MessageRepository messageRepository, String subject, String body, User from, User to) {
        Message message = new Message();
        message.setSubject(subject);
        message.setBody(body);
        message.setFrom(from);
        message.setTo(to);
        return messageRepository == null ? message : messageRepository.saveAndFlush(message);
    }

    /*
     * Generic assertions
     */

    public static <T> void assertObjectIdentity(T entity1, T entity2, T foundEntity, T nullEntity) throws Exception {
        assertThat(entity1.equals(nullEntity)).isFalse(); // null
        assertThat(entity1.equals("")).isFalse(); // other type
        assertThat(entity1.getClass().newInstance().equals(entity1.getClass().newInstance())).isFalse(); // instances with null id
        assertThat(entity1.equals(entity1.getClass().newInstance())).isFalse();
        assertThat(entity1.equals(entity2)).isFalse(); // other entity
        assertThat(foundEntity.equals(entity2)).isTrue();

        assertThat(entity1.hashCode()).isNotNull();
        assertThat(entity1.toString().length()).isGreaterThan(0);
    }

    /*
     * real EMail related
     */

    private static void assertThatMessageFromIs(MimeMessage mimeMessage, String email) throws Exception {
        Address[] senders = mimeMessage.getFrom();
        assertThat(senders).isNotNull();
        assertThat(senders.length).isGreaterThan(0);
        assertThat(senders[0]).isNotNull();
        assertThat(senders[0].toString()).isEqualTo(email);
    }

    private static void assertThatMessageRecipientIs(MimeMessage mimeMessage, String email) throws Exception {
        Address[] recipients = mimeMessage.getAllRecipients();
        assertThat(recipients).isNotNull();
        assertThat(recipients.length).isGreaterThan(0);
        assertThat(recipients[0]).isNotNull();
        assertThat(recipients[0].toString()).isEqualTo(email);
    }

    private static String getUserEmailSubject(MessageSource messageSource, User user, String subjectKey) {
        return messageSource.getMessage(subjectKey, null, Locale.forLanguageTag(user.getLangKey()));
    }

    private static String getUserEmailContent(SpringTemplateEngine templateEngine, User user, String contentTemplateName) {
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable("user", user);
        context.setVariable("baseUrl", "http://localhost:80/");

        return templateEngine.process(contentTemplateName, context);
    }

    public static void assertUserEmail(JHipsterProperties jHipsterProperties,
                                       MessageSource messageSource,
                                       SpringTemplateEngine templateEngine,
                                       MockMailSender mockMailSender,
                                       User user, String subjectKey, String contentTemplateName) throws Exception {

        Future<MimeMessage> futureMimeMessage = mockMailSender.getFutureMimeMessage();
        assertThat(futureMimeMessage).isNotNull();
        assertThat(futureMimeMessage.isDone()).isTrue();

        MimeMessage mimeMessage = futureMimeMessage.get();
        assertThat(mimeMessage).isNotNull();

        assertThatMessageFromIs(mimeMessage, jHipsterProperties.getMail().getFrom());
        assertThatMessageRecipientIs(mimeMessage, user.getEmail());

        assertThat(mimeMessage.getSubject()).isNotNull();
        assertThat(mimeMessage.getSubject()).isEqualTo(getUserEmailSubject(messageSource, user, subjectKey));

        assertThat(mimeMessage.getContent()).isNotNull();
        assertThat(
            cutQuotedUrls(mimeMessage.getContent().toString())
        ).isEqualTo(
            cutQuotedUrls(getUserEmailContent(templateEngine, user, contentTemplateName))
        );
    }

    //

    public static <T> void assertThatConstructorIsPrivate(Class<T> clazz) throws Exception {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
