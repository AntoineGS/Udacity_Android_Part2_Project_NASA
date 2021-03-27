package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.NetworkAsteroidContainer
import com.udacity.asteroidradar.api.asDatabaseModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.utils.getFormattedDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository (private val database: AsteroidDatabase){

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

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

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroidsRaw = AsteroidApi.retrofitService.getAsteroids(
                    startDate = getFormattedDate(),
                    endDate = getFormattedDate(7)
                )
                // Convert the result into an arraylist of Asteroid
                val asteroidsJSONObject = JSONObject(asteroidsRaw)
                val networkAsteroidContainer =
                        NetworkAsteroidContainer(parseAsteroidsJsonResult(asteroidsJSONObject))

                database.asteroidDao.insertAsteroid(*networkAsteroidContainer.asDatabaseModel())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}