package com.engagepoint.cws.apqd.repository;

import com.engagepoint.cws.apqd.domain.LookupMaritalStatus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LookupMaritalStatus entity.
 */
public interface LookupMaritalStatusRepository extends JpaRepository<LookupMaritalStatus,Long> {

}
