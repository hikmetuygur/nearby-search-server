package com.huygur.nearbysearch.service;

import com.huygur.nearbysearch.dto.request.PlaceAPIRequest;
import com.huygur.nearbysearch.dto.request.PlaceAPIRequest.LocationRestriction.Circle;
import com.huygur.nearbysearch.dto.request.PlaceAPIRequest.LocationRestriction.Center;
import com.huygur.nearbysearch.dto.response.PlaceAPI;
import com.huygur.nearbysearch.dto.response.PlaceResponse;
import com.huygur.nearbysearch.model.Place;
import com.huygur.nearbysearch.model.PlaceHistory;
import com.huygur.nearbysearch.repository.PlaceHistoryRepository;
import com.huygur.nearbysearch.repository.PlaceRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlaceService {

    @Value("${google.api.url}")
    private String apiUrl;

    private final PlaceHistoryRepository placeHistoryRepository;

    private final PlaceRepository placeRepository;

    private final RestTemplate restTemplate;

    public PlaceService(PlaceHistoryRepository placeHistoryRepository, PlaceRepository placeRepository, RestTemplate restTemplate) {
        this.placeHistoryRepository = placeHistoryRepository;
        this.placeRepository = placeRepository;
        this.restTemplate = restTemplate;
    }


    public List<Place> getPlaces(double latitude, double longitude, double radius) {

        return placeHistoryRepository
                .findByLatitudeAndLongitudeAndRadius(latitude, longitude, radius)
                .map(placeHistory -> placeRepository.findAllByIdIn(Arrays.asList(placeHistory.getPlaceIds().split(","))))
                .orElseGet(() -> searchNearby(latitude, longitude, radius));

    }

    public List<Place> searchNearby(double latitude, double longitude, double radius) {
        try {
            // Set request body
            PlaceAPIRequest requestBody = new PlaceAPIRequest(
                    new PlaceAPIRequest.LocationRestriction(
                            new Circle(
                                    new Center(latitude, longitude), radius)));


            // Create HttpEntity with headers and body
            HttpEntity<PlaceAPIRequest> requestEntity = new HttpEntity<>(requestBody);

            // Make the HTTP request with  restTemplate
            ResponseEntity<PlaceAPI> responseEntity = restTemplate
                    .exchange(apiUrl, HttpMethod.POST, requestEntity, PlaceAPI.class);

            // Handle the response
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                PlaceAPI responseBody = responseEntity.getBody();

                if (responseEntity.getBody() == null) {
                    throw new Exception("ResponseBody is null.");
                }

                List<PlaceResponse> placesFromGooglePlaces = responseBody.getPlaces();

                List<Place> places = placesFromGooglePlaces.stream()
                        .map(place -> new Place(
                                        place.getId(),
                                        place.getLocation().getLatitude(),
                                        place.getLocation().getLongitude(),
                                        place.getDisplayName().getText(),
                                        place.getDisplayName().getLanguageCode()
                                )
                        )
                        .collect(Collectors.toList());

                placeRepository.saveAll(places);

                var placeIds = places.stream().map(Place::getId).collect(Collectors.joining(","));
                placeHistoryRepository.save(new PlaceHistory(
                                UUID.randomUUID().toString(),
                                latitude,
                                longitude,
                                radius,
                                placeIds
                        )
                );

                return places;
            } else {
                System.err.println("Error while calling Google Places API. Status code: "
                        + ResponseEntity.status(responseEntity.getStatusCode()).build());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
