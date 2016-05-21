package com.engagepoint.cws.apqd.repository;

import com.engagepoint.cws.apqd.domain.MailBox;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the MailBox entity.
 */
public interface MailBoxRepository extends JpaRepository<MailBox,Long> {

}
