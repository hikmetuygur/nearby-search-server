package com.huygur.nearbysearch.controller;

import com.huygur.nearbysearch.model.Place;
import com.huygur.nearbysearch.service.PlaceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;

    }

    @GetMapping("/places")
    public List<Place> getPlaces(@RequestParam double latitude, @RequestParam double longitude, @RequestParam double radius) {
        return placeService.getPlaces(latitude, longitude, radius);
    }

}
