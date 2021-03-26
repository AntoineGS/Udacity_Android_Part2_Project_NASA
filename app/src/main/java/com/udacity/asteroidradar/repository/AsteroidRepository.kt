package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.PictureOfDay

class AsteroidRepository (private val database: AsteroidDatabase){

    suspend fun pictureOfDay(): PictureOfDay? {
        refreshPictureOfDayIfNeeded()
        return database.pictureOfDayDao.getPictureOfDay()?.asDomainModel()
    }

    private suspend fun refreshPictureOfDayIfNeeded() {
        // todo: it would be interesting to check if today's image is there, if not return the
        // old one while we fetch the new url, and using livedata have the image update
        if (database.pictureOfDayDao.getPictureOfDay() == null) {
            val pictureOfDay = AsteroidApi.retrofitService.getPictureOfTheDay()
            database.pictureOfDayDao.insertPictureOfDay(pictureOfDay.asDatabaseModel())
        }
    }
}