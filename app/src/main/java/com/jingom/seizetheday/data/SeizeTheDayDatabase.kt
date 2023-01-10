package com.jingom.seizetheday.data

import androidx.room.Database
import androidx.room.TypeConverters
import com.jingom.seizetheday.data.converter.FeelingTypeConverters

@Database(entities = [ThanksRecord::class], version = 1, exportSchema = false)
@TypeConverters(FeelingTypeConverters::class)
abstract class SeizeTheDayDatabase {
	abstract fun getThanksRecordDao(): ThanksRecordDao
}