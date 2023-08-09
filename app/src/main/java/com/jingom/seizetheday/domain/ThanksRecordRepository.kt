package com.jingom.seizetheday.domain

import androidx.paging.PagingData
import com.jingom.seizetheday.domain.model.ThanksRecord
import com.jingom.seizetheday.domain.model.ThanksRecordWithImages
import kotlinx.coroutines.flow.Flow

interface ThanksRecordRepository {
	suspend fun saveThanksRecord(thanksRecord: ThanksRecord): Long
	fun getThanksRecordsPagingFlow(startThanksId: Long?): Flow<PagingData<ThanksRecordWithImages>>
}