package com.huygur.nearbysearch.model

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class Place(

    @Id
    val id: String,

    val latitude: Double,

    val longitude: Double,

    val text: String?,

    val languageCode: String?
)
