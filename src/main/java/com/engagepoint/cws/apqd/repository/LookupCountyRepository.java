package com.engagepoint.cws.apqd.repository;

import com.engagepoint.cws.apqd.domain.LookupCounty;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LookupCounty entity.
 */
public interface LookupCountyRepository extends JpaRepository<LookupCounty,Long> {

}
