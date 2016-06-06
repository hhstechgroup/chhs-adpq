package com.engagepoint.cws.apqd.service;

import com.engagepoint.cws.apqd.domain.*;
import com.engagepoint.cws.apqd.repository.AuthorityRepository;
import com.engagepoint.cws.apqd.repository.MailBoxRepository;
import com.engagepoint.cws.apqd.repository.PersistentTokenRepository;
import com.engagepoint.cws.apqd.repository.UserRepository;
import com.engagepoint.cws.apqd.repository.search.UserSearchRepository;
import com.engagepoint.cws.apqd.security.AuthoritiesConstants;
import com.engagepoint.cws.apqd.security.SecurityUtils;
import com.engagepoint.cws.apqd.service.util.RandomUtil;
import com.engagepoint.cws.apqd.web.rest.MailResource;
import com.engagepoint.cws.apqd.web.rest.dto.ManagedUserDTO;
import com.engagepoint.cws.apqd.web.rest.dto.UserDTO;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserSearchRepository userSearchRepository;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private MailBoxRepository mailBoxRepository;

    @Inject
    private MailResource mailResource;

    public Optional<User> activateRegistration(String key) {
        LOGGER.debug("Activating user for activation key {}", key);
        userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                userSearchRepository.save(user);
                sendInvitationLetter(user.getLogin());
                attachSupportContacts(user.getLogin());
                LOGGER.debug("Activated user: {}", user);
                return user;
            });
        return Optional.empty();
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
       LOGGER.debug("Reset user password for reset key {}", key);

       return userRepository.findOneByResetKey(key)
            .filter(user -> {
                ZonedDateTime oneDayAgo = ZonedDateTime.now().minusHours(24);
                return user.getResetDate().isAfter(oneDayAgo);
           })
           .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                userRepository.save(user);
                return user;
           });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmail(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(ZonedDateTime.now());
                userRepository.save(user);
                return user;
            });
    }

    public User createUserInformation(UserDTO userDTO) {

        User newUser = new User();
        MailBox mailBox = prepareMailbox();
        mailBox.setUser(newUser);
        newUser.setMailBox(mailBox);
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.PARENT);
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        newUser.setLogin(userDTO.getLogin());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail().toLowerCase());
        newUser.setLangKey(userDTO.getLangKey());
        newUser.setSsnLast4Digits(userDTO.getSsnLast4Digits());
        newUser.setBirthDate(userDTO.getBirthDate());
        newUser.setGender(userDTO.getGender());
        newUser.setPhoneNumber(userDTO.getPhoneNumber());
        newUser.setCaseNumber(userDTO.getCaseNumber());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        userSearchRepository.save(newUser);
        LOGGER.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    private void sendInvitationLetter(String login) {
        Message invitation = new Message();

        String body;
        try {
            body = IOUtils.toString(getClass().getResourceAsStream("/templates/invitation.html"));
        } catch (IOException e) {
            throw new IllegalStateException("this should not happen", e);
        }

        invitation.setBody(body);
        invitation.setSubject("Welcome!");
        invitation.setFrom(userRepository.findOneByLogin("maryjenkins").get());
        invitation.setTo(userRepository.findOneByLogin(login).get());

        mailResource.sendInvitationLetter(invitation);
    }

    private void attachSupportContacts(String login) {
        User user = userRepository.findOneByLogin(login).get();
        User support = userRepository.findOneByLogin("worker").get();
        mailResource.updateUserContacts(user, support);
    }

    private MailBox prepareMailbox() {
        MailBox mailBox = new MailBox();

        Inbox inbox = new Inbox();
        inbox.setMailBox(mailBox);
        mailBox.setInbox(inbox);

        Outbox outbox = new Outbox();
        outbox.setMailBox(mailBox);
        mailBox.setOutbox(outbox);

        Draft draft = new Draft();
        draft.setMailBox(mailBox);
        mailBox.setDraft(draft);

        return mailBoxRepository.save(mailBox);
    }

    public User createUser(ManagedUserDTO managedUserDTO) {
        User user = new User();
        user.setLogin(managedUserDTO.getLogin());
        user.setFirstName(managedUserDTO.getFirstName());
        user.setLastName(managedUserDTO.getLastName());
        user.setEmail(managedUserDTO.getEmail());
        user.setSsnLast4Digits(managedUserDTO.getSsnLast4Digits());
        user.setBirthDate(managedUserDTO.getBirthDate());
        user.setGender(managedUserDTO.getGender());
        user.setPhoneNumber(managedUserDTO.getPhoneNumber());
        if (managedUserDTO.getLangKey() == null) {
            user.setLangKey("en"); // default language is English
        } else {
            user.setLangKey(managedUserDTO.getLangKey());
        }
        if (managedUserDTO.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>();
            managedUserDTO.getAuthorities().stream().forEach(
                authority -> authorities.add(authorityRepository.findOne(authority))
            );
            user.setAuthorities(authorities);
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(ZonedDateTime.now());
        user.setActivated(true);
        userRepository.save(user);
        userSearchRepository.save(user);
        LOGGER.debug("Created Information for User: {}", user);
        return user;
    }

    public void updateUserInformation(String firstName, String lastName, String email, String langKey,
        String ssnLast4Digits, LocalDate birthDate, LookupGender gender, String phoneNumber, Place place, String caseNumber) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUser().getUsername()).ifPresent(u -> {
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setEmail(email);
            u.setLangKey(langKey);
            u.setSsnLast4Digits(ssnLast4Digits);
            u.setBirthDate(birthDate);
            u.setPhoneNumber(phoneNumber);
            u.setGender(gender);
            u.setPlace(place);
            u.setCaseNumber(caseNumber);
            userRepository.save(u);
            userSearchRepository.save(u);
            LOGGER.debug("Changed Information for User: {}", u);
        });
    }

    public void deleteUserInformation(String login) {
        userRepository.findOneByLogin(login).ifPresent(u -> {
            userRepository.delete(u);
            userSearchRepository.delete(u);
            LOGGER.debug("Deleted User: {}", u);
        });
    }

    public void changePassword(String password) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUser().getUsername()).ifPresent(u -> {
            String encryptedPassword = passwordEncoder.encode(password);
            u.setPassword(encryptedPassword);
            userRepository.save(u);
            LOGGER.debug("Changed password for User: {}", u);
        });
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneByLogin(login).map(u -> {
            u.getAuthorities().size();
            return u;
        });
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUser().getUsername()).get();
        user.getAuthorities().size(); // eagerly load the association
        return user;
    }

    /**
     * Persistent Token are used for providing automatic authentication, they should be automatically deleted after
     * 30 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at midnight.
     * </p>
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void removeOldPersistentTokens() {
        LocalDate now = LocalDate.now();
        persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1)).stream().forEach(token -> {
            LOGGER.debug("Deleting token {}", token.getSeries());
            User user = token.getUser();
            user.getPersistentTokens().remove(token);
            persistentTokenRepository.delete(token);
        });
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        ZonedDateTime now = ZonedDateTime.now();
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
        for (User user : users) {
            LOGGER.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
            userSearchRepository.delete(user);
        }
    }
}
