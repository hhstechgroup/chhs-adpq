package com.engagepoint.cws.apqd.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.engagepoint.cws.apqd.domain.Message;
import com.engagepoint.cws.apqd.domain.User;
import com.engagepoint.cws.apqd.domain.enumeration.MessageStatus;
import com.engagepoint.cws.apqd.repository.MessageRepository;
import com.engagepoint.cws.apqd.repository.UserRepository;
import com.engagepoint.cws.apqd.repository.search.MessageSearchRepository;
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

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Message.
 */
@RestController
@RequestMapping("/api")
public class EMailResource {

    private final Logger log = LoggerFactory.getLogger(EMailResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private MessageRepository messageRepository;

    @Inject
    private MailBoxService mailBoxService;

    @Inject
    private MessageSearchRepository messageSearchRepository;

    @RequestMapping(value = "/emails/{directory}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Transactional(readOnly = true)
    public ResponseEntity<List<Message>> getMessages(@PathVariable EMailDirectory directory, Pageable pageable)
        throws URISyntaxException
    {
        Page<Message> page = null;
        User userTo = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        if (directory == EMailDirectory.inbox) {
            page = messageRepository.findAllByInboxIsNotNullAndReplyOnIsNullAndToIsOrderByDateUpdatedDesc(userTo, pageable);
        } else if (directory == EMailDirectory.sent) {
            page = messageRepository.findAllByOutboxIsNotNullAndReplyOnIsNullAndToIsOrderByDateUpdatedDesc(userTo, pageable);
        } else if (directory == EMailDirectory.drafts) {
            page = messageRepository.findAllByDraftIsNotNullAndReplyOnIsNullAndToIsOrderByDateCreatedDesc(userTo, pageable);
        } else if (directory == EMailDirectory.deleted) {
            page = messageRepository.findAllByDeletedIsNotNullAndReplyOnIsNullAndToIsOrderByDateUpdatedDesc(userTo, pageable);
        }

        if (page == null) {
            throw new URISyntaxException("page", "this should not happen");
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
            "/api/messages/" + directory);

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/emails/draft",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Message> saveMessage(@Valid @RequestBody Message message) throws URISyntaxException {
        log.debug("REST request to update Message : {}", message);
        if (message.getId() == null) {
            return createMessage(message);
        }
        Message result = messageRepository.save(enhanceMessage(message));
        mailBoxService.notifyClientAboutDraftsCount();
        messageSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("message", message.getId().toString()))
            .body(result);
    }

    @RequestMapping(value = "/emails/draft",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> sendMessage(@Valid @RequestBody Message message) throws URISyntaxException {

        User userTo = userRepository.findOneByLogin(message.getTo().getLogin()).get();
        User userFrom = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();

        message.setTo(userTo);
        message.setFrom(userFrom);
        message.setStatus(MessageStatus.NEW);
        message.setDateUpdated(ZonedDateTime.now());
        message.setDraft(null);
        message.setInbox(userTo.getMailBox().getInbox());
        message.setOutbox(userFrom.getMailBox().getOutbox());

        Message result = messageRepository.save(message);
        messageSearchRepository.save(result);

        mailBoxService.notifyClientAboutDraftsCount();
        mailBoxService.notifyClientAboutUnreadInboxCount(message);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Message> createMessage(@Valid @RequestBody Message message) throws URISyntaxException {
        log.debug("REST request to save Message : {}", message);
        if (message.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("message", "idexists",
                "A new message cannot already have an ID")).body(null);
        }
        Message result = messageRepository.save(enhanceMessage(message));
        mailBoxService.notifyClientAboutDraftsCount();
        messageSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/messages/draft" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("message", result.getId().toString()))
            .body(result);
    }

    private Message enhanceMessage(Message message) {
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
        message.setStatus(MessageStatus.NEW);
        message.setDateCreated(ZonedDateTime.now());
        message.setDraft(userFrom.getMailBox().getDraft());

        return message;
    }
}
