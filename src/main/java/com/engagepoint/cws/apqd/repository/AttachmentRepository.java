package com.engagepoint.cws.apqd.repository;

import com.engagepoint.cws.apqd.domain.Attachment;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Attachment entity.
 */
public interface AttachmentRepository extends JpaRepository<Attachment,Long> {

}
