package com.engagepoint.cws.apqd.repository;

import com.engagepoint.cws.apqd.domain.LookupGender;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LookupGender entity.
 */
public interface LookupGenderRepository extends JpaRepository<LookupGender,Long> {

}
