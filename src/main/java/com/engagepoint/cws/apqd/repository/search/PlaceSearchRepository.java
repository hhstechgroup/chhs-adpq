package com.engagepoint.cws.apqd.repository.search;

import com.engagepoint.cws.apqd.domain.Place;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Place entity.
 */
public interface PlaceSearchRepository extends ElasticsearchRepository<Place, Long> {
}
