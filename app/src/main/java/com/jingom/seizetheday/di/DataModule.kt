package com.jingom.seizetheday.di

import android.content.Context
import androidx.room.Room
import com.jingom.seizetheday.data.SeizeTheDayDatabase
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

}