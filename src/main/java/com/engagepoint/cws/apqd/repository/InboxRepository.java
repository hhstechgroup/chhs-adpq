package com.engagepoint.cws.apqd.repository;

import com.engagepoint.cws.apqd.domain.Inbox;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Inbox entity.
 */
public interface InboxRepository extends JpaRepository<Inbox,Long> {

}
