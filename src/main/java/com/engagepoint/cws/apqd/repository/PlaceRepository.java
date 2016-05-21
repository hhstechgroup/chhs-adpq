package com.engagepoint.cws.apqd.repository;

import com.engagepoint.cws.apqd.domain.Place;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Place entity.
 */
public interface PlaceRepository extends JpaRepository<Place,Long> {

}
