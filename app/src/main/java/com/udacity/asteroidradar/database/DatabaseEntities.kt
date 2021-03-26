package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
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