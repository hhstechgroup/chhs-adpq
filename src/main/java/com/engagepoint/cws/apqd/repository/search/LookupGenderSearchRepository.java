package com.engagepoint.cws.apqd.repository.search;

import com.engagepoint.cws.apqd.domain.LookupGender;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the LookupGender entity.
 */
public interface LookupGenderSearchRepository extends ElasticsearchRepository<LookupGender, Long> {
}
