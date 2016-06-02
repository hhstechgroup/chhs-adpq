package com.engagepoint.cws.apqd.repository.search;

import com.engagepoint.cws.apqd.domain.MessageThread;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MessageThreadSearchRepository extends ElasticsearchRepository<MessageThread, Long> {

}
