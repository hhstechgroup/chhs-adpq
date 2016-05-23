package com.engagepoint.cws.apqd.service;

import com.codahale.metrics.annotation.Timed;
import com.engagepoint.cws.apqd.domain.*;
import com.engagepoint.cws.apqd.repository.*;
import com.engagepoint.cws.apqd.repository.search.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class ElasticsearchIndexService {

    private final Logger log = LoggerFactory.getLogger(ElasticsearchIndexService.class);

    @Inject
    private EmailRepository emailRepository;

    @Inject
    private EmailSearchRepository emailSearchRepository;

    @Inject
    private InboxRepository inboxRepository;

    @Inject
    private InboxSearchRepository inboxSearchRepository;

    @Inject
    private LookupCountyRepository lookupCountyRepository;

    @Inject
    private LookupCountySearchRepository lookupCountySearchRepository;

    @Inject
    private LookupStateRepository lookupStateRepository;

    @Inject
    private LookupStateSearchRepository lookupStateSearchRepository;

    @Inject
    private MailBoxRepository mailBoxRepository;

    @Inject
    private MailBoxSearchRepository mailBoxSearchRepository;

    @Inject
    private MessageRepository messageRepository;

    @Inject
    private MessageSearchRepository messageSearchRepository;

    @Inject
    private OutboxRepository outboxRepository;

    @Inject
    private OutboxSearchRepository outboxSearchRepository;

    @Inject
    private PhoneRepository phoneRepository;

    @Inject
    private PhoneSearchRepository phoneSearchRepository;

    @Inject
    private PlaceRepository placeRepository;

    @Inject
    private PlaceSearchRepository placeSearchRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserSearchRepository userSearchRepository;

    @Inject
    private ElasticsearchTemplate elasticsearchTemplate;

    @Async
    @Timed
    public void reindexAll() {
        elasticsearchTemplate.deleteIndex(Email.class);
        if (emailRepository.count() > 0) {
            emailSearchRepository.save(emailRepository.findAll());
        }
        log.info("Elasticsearch: Indexed all emails");

        elasticsearchTemplate.deleteIndex(Inbox.class);
        if (inboxRepository.count() > 0) {
            inboxSearchRepository.save(inboxRepository.findAll());
        }
        log.info("Elasticsearch: Indexed all inboxs");

        elasticsearchTemplate.deleteIndex(LookupCounty.class);
        if (lookupCountyRepository.count() > 0) {
            lookupCountySearchRepository.save(lookupCountyRepository.findAll());
        }
        log.info("Elasticsearch: Indexed all lookupCountys");

        elasticsearchTemplate.deleteIndex(LookupState.class);
        if (lookupStateRepository.count() > 0) {
            lookupStateSearchRepository.save(lookupStateRepository.findAll());
        }
        log.info("Elasticsearch: Indexed all lookupStates");

        elasticsearchTemplate.deleteIndex(MailBox.class);
        if (mailBoxRepository.count() > 0) {
            mailBoxSearchRepository.save(mailBoxRepository.findAll());
        }
        log.info("Elasticsearch: Indexed all mailBoxs");

        elasticsearchTemplate.deleteIndex(Message.class);
        if (messageRepository.count() > 0) {
            messageSearchRepository.save(messageRepository.findAll());
        }
        log.info("Elasticsearch: Indexed all messages");

        elasticsearchTemplate.deleteIndex(Outbox.class);
        if (outboxRepository.count() > 0) {
            outboxSearchRepository.save(outboxRepository.findAll());
        }
        log.info("Elasticsearch: Indexed all outboxs");

        elasticsearchTemplate.deleteIndex(Phone.class);
        if (phoneRepository.count() > 0) {
            phoneSearchRepository.save(phoneRepository.findAll());
        }
        log.info("Elasticsearch: Indexed all phones");

        elasticsearchTemplate.deleteIndex(Place.class);
        if (placeRepository.count() > 0) {
            placeSearchRepository.save(placeRepository.findAll());
        }
        log.info("Elasticsearch: Indexed all places");

        elasticsearchTemplate.deleteIndex(User.class);
        if (userRepository.count() > 0) {
            userSearchRepository.save(userRepository.findAll());
        }
        log.info("Elasticsearch: Indexed all users");

        log.info("Elasticsearch: Successfully performed reindexing");
    }
}
