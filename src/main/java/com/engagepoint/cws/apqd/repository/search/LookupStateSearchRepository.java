package com.engagepoint.cws.apqd.repository.search;

import com.engagepoint.cws.apqd.domain.LookupState;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the LookupState entity.
 */
public interface LookupStateSearchRepository extends ElasticsearchRepository<LookupState, Long> {
}
