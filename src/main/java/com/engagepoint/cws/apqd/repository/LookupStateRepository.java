package com.engagepoint.cws.apqd.repository;

import com.engagepoint.cws.apqd.domain.LookupState;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LookupState entity.
 */
public interface LookupStateRepository extends JpaRepository<LookupState,Long> {

}
