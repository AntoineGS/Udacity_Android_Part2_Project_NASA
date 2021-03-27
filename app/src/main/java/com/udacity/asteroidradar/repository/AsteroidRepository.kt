package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

enum class AsteroidFilter{ALL, TODAY, WEEK}

class AsteroidRepository (private val database: AsteroidDatabase){

    private val _asteroidFilter: MutableLiveData<AsteroidFilter> =
            MutableLiveData(AsteroidFilter.ALL)
//    val asteroidFilter: LiveData<AsteroidFilter>
//        get() = _asteroidFilter

    val asteroids: LiveData<List<Asteroid>> = Transformations.switchMap(_asteroidFilter){ asteroidFilter ->
        when (asteroidFilter) {
            AsteroidFilter.ALL -> Transformations.map(database.asteroidDao.getAsteroids()) {
                it.asDomainModel()
            }
            AsteroidFilter.WEEK -> Transformations.map(database.asteroidDao.getWeekAsteroids()) {
                it.asDomainModel()
            }
            AsteroidFilter.TODAY -> Transformations.map(database.asteroidDao.getTodayAsteroids()) {
                it.asDomainModel()
            }
        }
    }

    suspend fun pictureOfDay(): PictureOfDay? {
        refreshPictureOfDayIfNeeded()
        return database.pictureOfDayDao.getPictureOfDay()?.asDomainModel()
    }

    private suspend fun refreshPictureOfDayIfNeeded() {
        // todo: it would be interesting to check if today's image is there, if not return the
        // old one while we fetch the new url, and using livedata have the image update
        withContext(Dispatchers.IO) {
            if (database.pictureOfDayDao.getPictureOfDay() == null) {
                val pictureOfDay = AsteroidApi.retrofitService.getPictureOfTheDay()
                database.pictureOfDayDao.insertPictureOfDay(pictureOfDay.asDatabaseModel())
            }
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

    fun updateFiler(asteroidFilter: AsteroidFilter) {
        _asteroidFilter.value = asteroidFilter
    }
}