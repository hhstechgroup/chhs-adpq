package com.engagepoint.cws.apqd.repository.search;

import com.engagepoint.cws.apqd.domain.Deleted;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Deleted entity.
 */
public interface DeletedSearchRepository extends ElasticsearchRepository<Deleted, Long> {
}
