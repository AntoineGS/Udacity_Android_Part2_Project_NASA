package com.udacity.asteroidradar.utils

import java.util.*

fun getCurrentDateWithoutTime(): Date {
    val calendar = Calendar.getInstance()
    calendar[Calendar.HOUR_OF_DAY] = 0
    calendar[Calendar.MINUTE] = 0
    calendar[Calendar.SECOND] = 0
    calendar[Calendar.MILLISECOND] = 0

    return calendar.time
}