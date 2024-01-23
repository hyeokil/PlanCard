package com.ssafy.backend.domain.place.repository;

import com.ssafy.backend.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceInfoRepository extends JpaRepository<Place, Long> {
}
