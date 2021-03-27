package com.udacity.asteroidradar.api

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.DatabasePictureOfDay
import com.udacity.asteroidradar.domain.Asteroid
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

@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val asteroids: List<NetworkAsteroid>)

@JsonClass(generateAdapter = true)
data class NetworkAsteroid(
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean)

fun NetworkAsteroid.asDomainModel(): Asteroid {
    return Asteroid(id, codename, closeApproachDate, absoluteMagnitude, estimatedDiameter,
            relativeVelocity, distanceFromEarth, isPotentiallyHazardous)
}

fun NetworkAsteroidContainer.asDomainModel(): List<Asteroid> {
    return asteroids.map {
        it.asDomainModel()
    }
}

fun NetworkAsteroid.asDatabaseModel(): DatabaseAsteroid {
    return DatabaseAsteroid(id, codename, closeApproachDate, absoluteMagnitude, estimatedDiameter,
        relativeVelocity, distanceFromEarth, isPotentiallyHazardous)
}

fun NetworkAsteroidContainer.asDatabaseModel(): Array<DatabaseAsteroid> {
    return asteroids.map {
        it.asDatabaseModel()
    }.toTypedArray()
}