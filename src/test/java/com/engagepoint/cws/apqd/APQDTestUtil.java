package com.engagepoint.cws.apqd;

import com.engagepoint.cws.apqd.domain.Deleted;
import com.engagepoint.cws.apqd.domain.Draft;
import com.engagepoint.cws.apqd.domain.Inbox;
import com.engagepoint.cws.apqd.domain.MailBox;
import com.engagepoint.cws.apqd.domain.Message;
import com.engagepoint.cws.apqd.domain.Outbox;
import com.engagepoint.cws.apqd.domain.User;
import com.engagepoint.cws.apqd.repository.AuthorityRepository;
import com.engagepoint.cws.apqd.repository.DeletedRepository;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public final class APQDTestUtil {
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

    /**
     * Usage:
     *      TestUtil.setCurrentUser(user);
     *      String login = SecurityUtils.getCurrentUserLogin();
     *      assertThat(login).isEqualTo(user.getLogin());
     *
     * @param user User
     */
    public static void setCurrentUser(User user) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(user.getLogin(), user.getPassword()));
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
        user.setPhoneNumber("1111-111-111");

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
        user.setPhoneNumber("1111-111-222");

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
    }

    public static void expectHasContact(ResultActions resultActions, User contact) throws Exception {
        ContactDTO contactDTO = new ContactDTO(contact);
        resultActions
            .andExpect(jsonPath("$.[*].login").value(hasItem(contactDTO.getLogin())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(contactDTO.getFirstName())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(contactDTO.getLastName())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(contactDTO.getPhone())))
            .andExpect(jsonPath("$.[*].roleDescription").value(hasItem(contactDTO.getRoleDescription())));
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
     * Deleted-related
     */

    public static Deleted prepareDeleted(DeletedRepository deletedRepository) {
        return deletedRepository.saveAndFlush(new Deleted());
    }

    public static Deleted setMessage(DeletedRepository deletedRepository, Deleted deleted, Message message) {
        return deletedRepository.saveAndFlush(deleted);
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

    public static MailBox prepareMailBox(MailBoxRepository mailBoxRepository, Inbox inbox, Outbox outbox, Deleted deleted, Draft draft, User user) {
        MailBox mailBox = new MailBox();
        mailBox.setInbox(inbox);
        mailBox.setOutbox(outbox);
        mailBox.setDraft(draft);
        mailBox.setUser(user);
        return mailBoxRepository.saveAndFlush(mailBox);
    }

    public static MailBox prepareMailBox(MailBoxRepository mailBoxRepository, InboxRepository inboxRepository,
                                         OutboxRepository outboxRepository, DeletedRepository deletedRepository,
                                         DraftRepository draftRepository) {
        return prepareMailBox(mailBoxRepository, prepareInbox(inboxRepository), prepareOutbox(outboxRepository),
            prepareDeleted(deletedRepository), prepareDraft(draftRepository), null);
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
}
