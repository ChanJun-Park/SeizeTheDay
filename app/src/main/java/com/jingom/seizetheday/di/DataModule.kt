package com.jingom.seizetheday.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jingom.seizetheday.data.SeizeTheDayDatabase
import com.jingom.seizetheday.data.ThanksRecordRepositoryImpl
import com.jingom.seizetheday.data.dao.ThanksRecordEntityDao
import com.jingom.seizetheday.domain.ThanksRecordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

	@Singleton
	@Provides
	fun provideSeizeTheDayDatabase(@ApplicationContext applicationContext: Context): SeizeTheDayDatabase {
		return Room.databaseBuilder(
			applicationContext,
			SeizeTheDayDatabase::class.java,
			"SeizeTheDayDatabase"
		).build()
	}

	@Singleton
	@Provides
	fun provideThanksRecordEntityDao(database: SeizeTheDayDatabase): ThanksRecordEntityDao {
		return database.getThanksRecordEntityDao()
	}

	@Singleton
	@Provides
	fun provideThanksRecordRepository(thanksRecordEntityDao: ThanksRecordEntityDao): ThanksRecordRepository {
		return ThanksRecordRepositoryImpl(thanksRecordEntityDao)
	}
}