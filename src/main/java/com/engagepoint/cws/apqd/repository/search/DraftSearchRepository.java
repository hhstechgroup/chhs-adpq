package com.engagepoint.cws.apqd.repository.search;

import com.engagepoint.cws.apqd.domain.Draft;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Draft entity.
 */
public interface DraftSearchRepository extends ElasticsearchRepository<Draft, Long> {
}
