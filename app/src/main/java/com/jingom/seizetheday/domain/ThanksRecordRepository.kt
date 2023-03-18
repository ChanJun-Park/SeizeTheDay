package com.jingom.seizetheday.domain

import androidx.paging.PagingData
import com.jingom.seizetheday.domain.model.AttachedImage
import com.jingom.seizetheday.domain.model.ThanksRecord
import kotlinx.coroutines.flow.Flow

interface ThanksRecordRepository {
	suspend fun saveThanksRecord(thanksRecord: ThanksRecord): Long

	suspend fun saveAttachedImages(attachedImages: List<AttachedImage>)

	fun getThanksRecordsFlow(): Flow<List<ThanksRecord>>

	fun getThanksRecordsPagingFlow(startThanksId: Long?): Flow<PagingData<ThanksRecord>>
}