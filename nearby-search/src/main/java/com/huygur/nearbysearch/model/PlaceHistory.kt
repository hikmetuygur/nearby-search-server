package com.huygur.nearbysearch.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class PlaceHistory(

    @Id
    val id: String,

    val latitude: Double,

    val longitude: Double,

    val radius: Double,

    @Column(columnDefinition = "TEXT")
    val placeIds: String
)
