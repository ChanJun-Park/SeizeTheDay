package com.jingom.seizetheday.di.data

import android.content.Context
import com.jingom.seizetheday.data.ThanksRecordRepositoryImpl
import com.jingom.seizetheday.data.db.dao.ThanksRecordEntityDao
import com.jingom.seizetheday.data.media.LocalMediaImageCursorLoader
import com.jingom.seizetheday.data.media.LocalMediaImageLoaderImpl
import com.jingom.seizetheday.di.coroutine.IoDispatcher
import com.jingom.seizetheday.domain.ThanksRecordRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
	@Singleton
	@Provides
	fun provideThanksRecordRepository(thanksRecordEntityDao: ThanksRecordEntityDao): ThanksRecordRepository {
		return ThanksRecordRepositoryImpl(thanksRecordEntityDao)
	}

	@Singleton
	@Provides
	fun provideLocalMediaImageCursorLoader(@ApplicationContext applicationContext: Context): LocalMediaImageCursorLoader {
		return LocalMediaImageCursorLoader(applicationContext)
	}

	@Singleton
	@Provides
	fun provideLocalMediaImageLoader(
		localMediaImageCursorLoader: LocalMediaImageCursorLoader,
		@IoDispatcher dispatcher: CoroutineDispatcher
	): LocalMediaImageLoaderImpl {
		return LocalMediaImageLoaderImpl(localMediaImageCursorLoader, dispatcher)
	}
}