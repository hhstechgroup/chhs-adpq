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
import com.engagepoint.cws.apqd.web.websocket.MailBoxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * REST controller for managing Message.
 */
@RestController
@RequestMapping("/api")
public class DraftMessageResource {

    private final Logger log = LoggerFactory.getLogger(DraftMessageResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private MessageRepository messageRepository;

    @Inject
    private MailBoxService mailBoxService;

    @Inject
    private MessageSearchRepository messageSearchRepository;

    /**
     * POST  /messages -> Create a new message.
     */
    @RequestMapping(value = "/messages/draft",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Message> createMessage(@Valid @RequestBody Message message) throws URISyntaxException {
        log.debug("REST request to save Message : {}", message);
        if (message.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("message", "idexists",
                "A new message cannot already have an ID")).body(null);
        }
        Message result = messageRepository.save(enhanceMessage(message));
        mailBoxService.updateDraftCount();
        messageSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/messages/draft" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("message", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /messages -> Updates an existing message.
     */
    @RequestMapping(value = "/messages/draft",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Message> updateMessage(@Valid @RequestBody Message message) throws URISyntaxException {
        log.debug("REST request to update Message : {}", message);
        if (message.getId() == null) {
            return createMessage(message);
        }
        Message result = messageRepository.save(enhanceMessage(message));
        mailBoxService.updateDraftCount();
        messageSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("message", message.getId().toString()))
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
        message.setDraft(userFrom.getMailBox().getDraft());

        return message;
    }
}
