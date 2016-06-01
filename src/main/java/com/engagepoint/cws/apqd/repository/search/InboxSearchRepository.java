package com.engagepoint.cws.apqd.repository.search;

import com.engagepoint.cws.apqd.domain.Inbox;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Inbox entity.
 */
public interface InboxSearchRepository extends ElasticsearchRepository<Inbox, Long> {
}
