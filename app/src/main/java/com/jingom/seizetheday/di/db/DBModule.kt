package com.jingom.seizetheday.di.db

import android.content.Context
import androidx.room.Room
import com.jingom.seizetheday.data.db.SeizeTheDayDatabase
import com.jingom.seizetheday.data.db.converter.DateTypeConverters
import com.jingom.seizetheday.data.db.converter.DateTypeConvertersImpl
import com.jingom.seizetheday.data.db.dao.AttachedImageEntityDao
import com.jingom.seizetheday.data.db.dao.ThanksRecordEntityDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DBModule {

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
	fun provideAttachedImageEntityDao(database: SeizeTheDayDatabase): AttachedImageEntityDao {
		return database.getAttachedImageEntityDao()
	}

	@Singleton
	@Provides
	fun provideDateTypeConverters(): DateTypeConverters {
		return DateTypeConvertersImpl()
	}
}