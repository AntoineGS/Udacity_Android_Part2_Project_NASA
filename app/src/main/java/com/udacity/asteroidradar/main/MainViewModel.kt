package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.asDomainModel
import com.udacity.asteroidradar.database.DatabasePictureOfDay
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

enum class PictureOfDayAPIStatus {LOADING, ERROR, DONE}

class MainViewModel(application: Application) : AndroidViewModel(application) {

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
                _pictureOfDay.value = AsteroidRepository(getDatabase(application)).pictureOfDay()
                _status.value = PictureOfDayAPIStatus.DONE
            } catch (e: Exception) {
                _status.value = PictureOfDayAPIStatus.ERROR
            }
        }
    }
}