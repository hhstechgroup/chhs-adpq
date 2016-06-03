package com.engagepoint.cws.apqd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.engagepoint.cws.apqd.domain.Message;
import com.engagepoint.cws.apqd.domain.MessageThread;
import com.engagepoint.cws.apqd.domain.User;
import com.engagepoint.cws.apqd.domain.enumeration.MessageStatus;
import com.engagepoint.cws.apqd.repository.MailBoxRepository;
import com.engagepoint.cws.apqd.repository.MessageRepository;
import com.engagepoint.cws.apqd.repository.UserRepository;
import com.engagepoint.cws.apqd.repository.search.MessageSearchRepository;
import com.engagepoint.cws.apqd.repository.search.MessageThreadSearchRepository;
import com.engagepoint.cws.apqd.security.SecurityUtils;
import com.engagepoint.cws.apqd.web.rest.util.HeaderUtil;
import com.engagepoint.cws.apqd.web.rest.util.PaginationUtil;
import com.engagepoint.cws.apqd.web.websocket.MailBoxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * REST controller for managing Message.
 */
@RestController
@RequestMapping("/api")
public class MailResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private MessageRepository messageRepository;

    @Inject
    private MailBoxService mailBoxService;

    @Inject
    private MailBoxRepository mailBoxRepository;

    @Inject
    private MessageSearchRepository messageSearchRepository;

    @Inject
    private MessageThreadSearchRepository messageThreadSearchRepository;

    @PostConstruct
    public void init() {
        messageThreadSearchRepository.save(new MessageThread());
    }

    @RequestMapping(value = "/mails/{directory}/{search}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<Message>> getMessages(@PathVariable EMailDirectory directory,
                                                     @PathVariable String search, Pageable pageable)
        throws URISyntaxException {

        Page<Message> page;

        if (search.equals("-1")) {
            page = loadMessagesViaSQL(directory, pageable);
        } else {
            page = loadMessagesViaElastic(directory, search, pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
            "/api/messages/" + directory);

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/mails/thread/{messageId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<MessageThread> messageThreadSearchRepository(@PathVariable Long messageId) {
        return new ResponseEntity<>(findOrCreateMessageThreadByMessageId(messageId), HttpStatus.OK);
    }

    @RequestMapping(value = "/mails/draft",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Message> saveMessage(@Valid @RequestBody Message message) throws URISyntaxException {
        LOGGER.debug("REST request to update Message : {}", message);
        if (message.getId() == null) {
            return createMessage(message);
        }
        Message result = messageRepository.save(enrichDraftMessage(message));
        mailBoxService.notifyClientAboutDraftsCount();
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("message", message.getId().toString()))
            .body(result);
    }

    @RequestMapping(value = "/mails/draft",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<Void> sendMessage(@Valid @RequestBody Message message) throws URISyntaxException {
        User userTo = userRepository.findOneByLogin(message.getTo().getLogin()).get();
        User userFrom = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        updateUserContacts(userFrom, userTo);
        moveMessageFromDraftToInbox(message, userTo, userFrom);
        updateUnreadCount(message);
        updateMessageThread(message);

        mailBoxService.notifyClientAboutDraftsCount();
        mailBoxService.notifyClientAboutUnreadInboxCount(message.getTo());

        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/mails/confirm",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional
    public ResponseEntity<Void> confirmReading(@RequestBody Message message) throws URISyntaxException {
        setReadStatusToAllMessagesInThread(message);
        mailBoxService.notifyClientAboutDraftsCount();
        mailBoxService.notifyClientAboutUnreadInboxCount(message.getTo());

        return ResponseEntity.ok().build();
    }

    @Transactional
    public void sendInvitationLetter(Message message) {
        User userTo = userRepository.findOneByLogin(message.getTo().getLogin()).get();
        User userFrom = userRepository.findOneByLogin(message.getFrom().getLogin()).get();

        updateUserContacts(userFrom, userTo);
        moveMessageFromDraftToInbox(message, userTo, userFrom);
        updateUnreadCount(message);
        updateMessageThread(message);
    }

    public ResponseEntity<Message> createMessage(@Valid @RequestBody Message message) throws URISyntaxException {
        LOGGER.debug("REST request to save Message : {}", message);
        if (message.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("message", "idexists",
                "A new message cannot already have an ID")).body(null);
        }
        Message result = messageRepository.save(enrichDraftMessage(message));
        mailBoxService.notifyClientAboutDraftsCount();
        return ResponseEntity.created(new URI("/api/messages/draft" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("message", result.getId().toString()))
            .body(result);
    }

    private Page<Message> loadMessagesViaElastic(EMailDirectory directory, String search, Pageable pageable) {
        String query = null;
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        if (directory == EMailDirectory.INBOX) {
            query = "+body:*" + search + "* +to.login:" + user.getLogin();
        } else if (directory == EMailDirectory.SENT) {
            query = "+body:*" + search + "* +from.login:" + user.getLogin();
        } else if (directory == EMailDirectory.DRAFTS) {
            throw new UnsupportedOperationException("not implemented yet");
        } else if (directory == EMailDirectory.DELETED) {
            throw new UnsupportedOperationException("not implemented yet");
        }

        return messageSearchRepository.search(queryStringQuery(query), pageable);
    }

    private Page<Message> loadMessagesViaSQL(EMailDirectory directory, Pageable pageable) {
        Page<Message> page = null;
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        if (directory == EMailDirectory.INBOX) {
            page = messageRepository.findAllByInboxIsNotNullAndReplyOnIsNullAndToIsOrderByDateUpdatedDesc(user, pageable);
        } else if (directory == EMailDirectory.SENT) {
            page = messageRepository.findAllByOutboxIsNotNullAndFromIsOrderByDateUpdatedDesc(user, pageable);
        } else if (directory == EMailDirectory.DRAFTS) {
            page = messageRepository.findAllByDraftIsNotNullAndFromIsOrderByDateCreatedDesc(user, pageable);
        } else if (directory == EMailDirectory.DELETED) {
            page = messageRepository.findAllByDeletedIsNotNullAndReplyOnIsNullAndToIsOrderByDateUpdatedDesc(user, pageable);
        }

        return page;
    }

    private Message enrichDraftMessage(Message message) {
        if (message.getTo() != null && message.getTo().getLogin() != null) {
            Optional<User> userTo = userRepository.findOneByLogin(message.getTo().getLogin());
            if (userTo.isPresent()) {
                message.setTo(userTo.get());
            } else {
                message.setTo(null);
            }
        }

        User userFrom = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        message.setFrom(userFrom);
        message.setStatus(MessageStatus.DRAFT);
        message.setDateCreated(ZonedDateTime.now());
        message.setDraft(userFrom.getMailBox().getDraft());

        return message;
    }

    private void updateUserContacts(User userFrom, User userTo) {
        boolean updateTo = true;
        boolean updateFrom = true;

        for (User user : userFrom.getMailBox().getContacts()) {
            if (user.equals(userTo)) {
                updateFrom = false;
                break;
            }
        }

        for (User user : userTo.getMailBox().getContacts()) {
            if (user.equals(userFrom)) {
                updateTo = false;
                break;
            }
        }

        if (updateFrom) {
            userFrom.getMailBox().getContacts().add(userTo);
            mailBoxRepository.save(userFrom.getMailBox());
        }

        if (updateTo) {
            userTo.getMailBox().getContacts().add(userFrom);
            mailBoxRepository.save(userTo.getMailBox());
        }
    }

    private void moveMessageFromDraftToInbox(Message message, User userTo, User userFrom) {
        message.setTo(userTo);
        message.setFrom(userFrom);
        message.setStatus(MessageStatus.UNREAD);
        message.setDateUpdated(ZonedDateTime.now());
        message.setDraft(null);
        message.setInbox(userTo.getMailBox().getInbox());
        message.setOutbox(userFrom.getMailBox().getOutbox());
        messageRepository.save(message);
    }

    private void updateUnreadCount(Message message) {
        Long rootId = message.getReplyOn() != null ? message.getReplyOn().getId() : message.getId();

        MessageThread thread = findOrCreateMessageThreadByMessageId(rootId);
        Message root = thread.getThread().get(0);

        int unread = root.getUnreadMessagesCount() + 1;
        ZonedDateTime updated = ZonedDateTime.now();

        root.setDateUpdated(updated);
        root.setUnreadMessagesCount(unread);
        messageThreadSearchRepository.save(thread);

        root = messageRepository.findOne(root.getId());
        root.setUnreadMessagesCount(unread);
        root.setDateUpdated(updated);
        messageRepository.save(root);
    }

    private void setReadStatusToAllMessagesInThread(Message message) {
        MessageThread thread = findOrCreateMessageThreadByMessageId(message.getId());
        Message root = thread.getThread().get(0);
        root.setUnreadMessagesCount(0);

        for (Message msg : thread.getThread()) {
            if (msg.getStatus() == MessageStatus.UNREAD) {
                msg.setStatus(MessageStatus.READ);
                msg.setDateRead(ZonedDateTime.now());

                Message saved = messageRepository.findOne(msg.getId());
                saved.setStatus(MessageStatus.READ);
                saved.setDateRead(msg.getDateRead());
                messageRepository.save(saved);
                messageSearchRepository.save(msg);
            }
        }

        root = messageRepository.findOne(root.getId());
        root.setUnreadMessagesCount(0);
        messageRepository.save(root);
        messageThreadSearchRepository.save(thread);
    }

    private void updateMessageThread(Message message) {
        MessageThread thread;

        if (message.getReplyOn() != null) {
            thread = findOrCreateMessageThreadByMessageId(message.getReplyOn().getId());
            thread.addMessage(message);
            updateInbox(message, thread);

        } else {
            thread = findOrCreateMessageThreadByMessageId(message.getId());
        }

        messageSearchRepository.save(message);
        messageThreadSearchRepository.save(thread);
    }

    private void updateInbox(Message message, MessageThread thread) {
        Message root = thread.getThread().get(0);
        if (root.getInbox() == null && root.getFrom().equals(message.getTo())) {
            Message one = messageRepository.findOne(root.getId());
            User to = userRepository.findOne(message.getTo().getId());
            one.setInbox(to.getMailBox().getInbox());
            messageRepository.save(one);
        }
    }

    private MessageThread findOrCreateMessageThreadByMessageId(Long messageId) {
        MessageThread thread;

        String query = "+thread.id:" + messageId;
        Iterator<MessageThread> iterator = messageThreadSearchRepository.search(
            queryStringQuery(query)).iterator();

        if (iterator.hasNext()) {
            thread = iterator.next();
        } else {
            thread = new MessageThread();
            thread.addMessage(messageRepository.findOne(messageId));
            messageThreadSearchRepository.save(thread);
        }

        return thread;
    }
}
