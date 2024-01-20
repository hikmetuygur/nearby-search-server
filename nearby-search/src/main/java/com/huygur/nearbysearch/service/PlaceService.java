package com.huygur.nearbysearch.service;

import com.huygur.nearbysearch.model.Place;
import com.huygur.nearbysearch.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {

    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }


    public List<Place> getPlaces(double latitude, double longitude, double radius) {

        List<Place> places = placeRepository.findByLatitudeAndLongitudeAndRadius(latitude, longitude, radius);


        return places;
    }

}
