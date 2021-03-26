package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.*

@Dao
interface PictureOfDayDao {
    // I could not figure out how to get the date without the time in sqlite but could not..
    // This takes the record where the difference between the date in the db (at 00:00:00) and now
    // is less than a day. The LIMIT 1 is extra protection in case something were to go wrong to
    // prevent multiple rows in a singleton select
    @Query("select * from databasepictureofday where (strftime('%s','now') - date/1000 - (24*60*60)) <= 0 LIMIT 1")
    suspend fun getPictureOfDay(): DatabasePictureOfDay?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPictureOfDay(vararg pictureOfDay: DatabasePictureOfDay)
}

private lateinit var INSTANCE: AsteroidDatabase

@Database(entities = [DatabasePictureOfDay::class], version = 1)
@TypeConverters(Converters::class)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val pictureOfDayDao: PictureOfDayDao
}

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidDatabase::class.java,
                "AsteroidDatabase"
            ).build()
        }
    }

    return INSTANCE
}