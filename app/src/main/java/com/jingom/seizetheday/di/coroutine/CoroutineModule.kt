package com.jingom.seizetheday.di.coroutine

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CoroutineModule {

	@Provides
	@Singleton
	@DefaultDispatcher
	fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

	@Provides
	@Singleton
	@IoDispatcher
	fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

	@Provides
	@Singleton
	@ApplicationScope
	fun providesCoroutineScope(@DefaultDispatcher dispatcher: CoroutineDispatcher): CoroutineScope {
		return CoroutineScope(SupervisorJob() + dispatcher)
	}
}