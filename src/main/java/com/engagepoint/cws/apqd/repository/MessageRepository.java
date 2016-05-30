package com.engagepoint.cws.apqd.repository;

import com.engagepoint.cws.apqd.domain.Message;
import com.engagepoint.cws.apqd.domain.User;
import com.engagepoint.cws.apqd.domain.enumeration.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Message entity.
 */
public interface MessageRepository extends JpaRepository<Message,Long> {

    Long countByDraftIsNotNullAndFrom(User user);

    Long countByInboxIsNotNullAndToAndStatus(User to, MessageStatus status);
}
