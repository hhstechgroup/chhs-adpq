package com.engagepoint.cws.apqd.repository;

import com.engagepoint.cws.apqd.domain.Email;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Email entity.
 */
public interface EmailRepository extends JpaRepository<Email,Long> {

}
