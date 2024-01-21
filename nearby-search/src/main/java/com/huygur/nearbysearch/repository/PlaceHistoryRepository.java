package com.huygur.nearbysearch.repository;

import com.huygur.nearbysearch.model.PlaceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlaceHistoryRepository extends JpaRepository<PlaceHistory, String> {

    Optional<PlaceHistory> findByLatitudeAndLongitudeAndRadius(double latitude, double longitude, double radius);
}
