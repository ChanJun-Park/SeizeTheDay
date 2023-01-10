package com.jingom.seizetheday.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jingom.seizetheday.data.converter.FeelingTypeConverters

@Database(entities = [ThanksRecordEntity::class], version = 1, exportSchema = false)
@TypeConverters(FeelingTypeConverters::class)
abstract class SeizeTheDayDatabase: RoomDatabase() {
	abstract fun getThanksRecordEntityDao(): ThanksRecordEntityDao
}