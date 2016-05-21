package com.engagepoint.cws.apqd.repository.search;

import com.engagepoint.cws.apqd.domain.Message;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Message entity.
 */
public interface MessageSearchRepository extends ElasticsearchRepository<Message, Long> {
}
