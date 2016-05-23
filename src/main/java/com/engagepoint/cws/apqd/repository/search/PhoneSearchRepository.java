package com.engagepoint.cws.apqd.repository.search;

import com.engagepoint.cws.apqd.domain.Phone;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Phone entity.
 */
public interface PhoneSearchRepository extends ElasticsearchRepository<Phone, Long> {
}
