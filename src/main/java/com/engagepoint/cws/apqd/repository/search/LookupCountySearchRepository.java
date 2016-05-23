package com.engagepoint.cws.apqd.repository.search;

import com.engagepoint.cws.apqd.domain.LookupCounty;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the LookupCounty entity.
 */
public interface LookupCountySearchRepository extends ElasticsearchRepository<LookupCounty, Long> {
}
