package com.engagepoint.cws.apqd.repository;

import com.engagepoint.cws.apqd.domain.Deleted;
import com.engagepoint.cws.apqd.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Deleted entity.
 */
public interface DeletedRepository extends JpaRepository<Deleted,Long> {

    Deleted findOneByMessage(Message message);
}
