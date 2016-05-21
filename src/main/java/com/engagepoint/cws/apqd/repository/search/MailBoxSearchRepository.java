package com.engagepoint.cws.apqd.repository.search;

import com.engagepoint.cws.apqd.domain.MailBox;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the MailBox entity.
 */
public interface MailBoxSearchRepository extends ElasticsearchRepository<MailBox, Long> {
}
