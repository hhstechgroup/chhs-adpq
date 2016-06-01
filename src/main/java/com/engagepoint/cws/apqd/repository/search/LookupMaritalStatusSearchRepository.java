package com.engagepoint.cws.apqd.repository.search;

import com.engagepoint.cws.apqd.domain.LookupMaritalStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the LookupMaritalStatus entity.
 */
public interface LookupMaritalStatusSearchRepository extends ElasticsearchRepository<LookupMaritalStatus, Long> {
}
