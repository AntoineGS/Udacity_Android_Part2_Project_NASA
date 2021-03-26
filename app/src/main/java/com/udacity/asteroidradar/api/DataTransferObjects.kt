package com.udacity.asteroidradar.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabasePictureOfDay
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.utils.getCurrentDateWithoutTime
import java.util.*

@JsonClass(generateAdapter = true)
data class NetworkPictureOfDay (
        @Json(name = "media_type")
        val mediaType: String,
        val title: String,
        val url: String
)

fun NetworkPictureOfDay.asDomainModel(): PictureOfDay {
    return PictureOfDay(mediaType, title, url)
}

fun NetworkPictureOfDay.asDatabaseModel(): DatabasePictureOfDay {
    return DatabasePictureOfDay(getCurrentDateWithoutTime(), mediaType, title, url)
}