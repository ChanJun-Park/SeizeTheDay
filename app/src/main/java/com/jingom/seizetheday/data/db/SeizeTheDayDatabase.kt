package com.jingom.seizetheday.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jingom.seizetheday.data.db.converter.DateTypeConverters
import com.jingom.seizetheday.data.db.converter.FeelingTypeConverters
import com.jingom.seizetheday.data.db.dao.ThanksRecordEntityDao
import com.jingom.seizetheday.data.db.model.ThanksRecordEntity

@Database(entities = [ThanksRecordEntity::class], version = 1, exportSchema = false)
@TypeConverters(FeelingTypeConverters::class, DateTypeConverters::class)
abstract class SeizeTheDayDatabase: RoomDatabase() {
	abstract fun getThanksRecordEntityDao(): ThanksRecordEntityDao
}