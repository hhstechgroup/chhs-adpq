package com.engagepoint.cws.apqd.repository.search;

import com.engagepoint.cws.apqd.domain.Outbox;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Outbox entity.
 */
public interface OutboxSearchRepository extends ElasticsearchRepository<Outbox, Long> {
}
