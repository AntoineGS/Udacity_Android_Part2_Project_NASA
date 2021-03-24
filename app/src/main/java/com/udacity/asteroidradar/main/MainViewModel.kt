package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.AsteroidApiService
import com.udacity.asteroidradar.api.PictureOfDay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

enum class PictureOfDayAPIStatus {LOADING, ERROR, DONE}

class MainViewModel : ViewModel() {

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _status = MutableLiveData<PictureOfDayAPIStatus>()
    val status: LiveData<PictureOfDayAPIStatus>
        get() = _status

    init {
        viewModelScope.launch {
            _status.value = PictureOfDayAPIStatus.LOADING
            try {
                _pictureOfDay.value = AsteroidApi.retrofitService.getPictureOfTheDay()
                _status.value = PictureOfDayAPIStatus.DONE
            } catch (e: Exception) {
                _status.value = PictureOfDayAPIStatus.ERROR
            }
        }
    }
}