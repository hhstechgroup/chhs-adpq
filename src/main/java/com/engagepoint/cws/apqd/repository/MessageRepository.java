package com.engagepoint.cws.apqd.repository;

import com.engagepoint.cws.apqd.domain.Message;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Message entity.
 */
public interface MessageRepository extends JpaRepository<Message,Long> {

}
