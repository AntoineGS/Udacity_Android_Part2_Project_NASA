package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import java.util.Date

@Entity
data class DatabasePictureOfDay constructor (
    @PrimaryKey
    val date: Date, // might as well store the date in case we want to prevent fetching it 2x
    val mediaType: String,
    val title: String,
    val url: String
)

fun DatabasePictureOfDay.asDomainModel(): PictureOfDay {
    return PictureOfDay(mediaType, title, url)
}

@Entity
data class DatabaseAsteroid constructor(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

fun DatabaseAsteroid.asDomainModel(): Asteroid {
    return Asteroid(id, codename, closeApproachDate, absoluteMagnitude, estimatedDiameter,
            relativeVelocity, distanceFromEarth, isPotentiallyHazardous)
}

fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
        it.asDomainModel()
    }
}