package com.engagepoint.cws.apqd.repository;

import com.engagepoint.cws.apqd.domain.Phone;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Phone entity.
 */
public interface PhoneRepository extends JpaRepository<Phone,Long> {

}
