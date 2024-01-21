package com.huygur.nearbysearch.dto.request

data class PlaceRequest(
    val latitude: Double,
    val longitude: Double,
    val radius: Double
)
