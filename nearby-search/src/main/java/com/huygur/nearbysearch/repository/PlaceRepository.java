package com.huygur.nearbysearch.repository;

import com.huygur.nearbysearch.model.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaceRepository extends JpaRepository<Place, String> {
    List<Place> findByLatitudeAndLongitudeAndRadius(double latitude, double longitude, double radius);
}