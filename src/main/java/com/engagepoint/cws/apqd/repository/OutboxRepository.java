package com.engagepoint.cws.apqd.repository;

import com.engagepoint.cws.apqd.domain.Outbox;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Outbox entity.
 */
public interface OutboxRepository extends JpaRepository<Outbox,Long> {

}
