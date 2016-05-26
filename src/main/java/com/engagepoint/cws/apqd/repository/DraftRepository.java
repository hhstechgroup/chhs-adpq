package com.engagepoint.cws.apqd.repository;

import com.engagepoint.cws.apqd.domain.Draft;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Draft entity.
 */
public interface DraftRepository extends JpaRepository<Draft,Long> {

}
