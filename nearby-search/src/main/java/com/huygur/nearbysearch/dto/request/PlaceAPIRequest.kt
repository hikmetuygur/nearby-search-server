package com.huygur.nearbysearch.dto.request

data class PlaceAPIRequest(
    val locationRestriction: LocationRestriction
) {
    data class LocationRestriction(
        val circle: Circle
    ) {
        data class Circle(
            val center: Center,
            val radius: Double
        )
        data class Center(
            val latitude: Double,
            val longitude: Double
        )
    }
}

