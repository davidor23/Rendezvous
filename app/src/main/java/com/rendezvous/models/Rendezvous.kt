package com.rendezvous.models

import java.io.Serializable

/**
 * A Data Model Class for Rendezvous details. We will you this data class in all over the project even when
 * dealing with local SQLite database.
 */
data class RendezvousModel(
    val id: Int,
    val title: String,
    val image: String,
    val description: String,
    val date: String,
    val location: String,
    val latitude: Double,
    val longitude: Double
) : Serializable