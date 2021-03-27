package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidFilter
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

enum class PictureOfDayAPIStatus {LOADING, ERROR, DONE}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)
    val asteroids = asteroidRepository.asteroids

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _status = MutableLiveData<PictureOfDayAPIStatus>()
    val status: LiveData<PictureOfDayAPIStatus>
        get() = _status

    init {
        viewModelScope.launch {
            _status.value = PictureOfDayAPIStatus.LOADING // todo: use the status, lol
            try {
                _pictureOfDay.value = asteroidRepository.pictureOfDay()
                _status.value = PictureOfDayAPIStatus.DONE
            } catch (e: Exception) {
                _status.value = PictureOfDayAPIStatus.ERROR
            }
        }

        viewModelScope.launch {
            asteroidRepository.refreshAsteroids()
        }
    }

    fun updateFilter(asteroidFilter: AsteroidFilter) {
        asteroidRepository.updateFiler(asteroidFilter)
    }
}