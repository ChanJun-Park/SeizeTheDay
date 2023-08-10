package com.jingom.seizetheday.domain

import androidx.paging.PagingData
import com.jingom.seizetheday.domain.model.ThanksRecord
import com.jingom.seizetheday.domain.model.ThanksRecordWithImages
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface ThanksRecordRepository {
	suspend fun saveThanksRecord(thanksRecord: ThanksRecord): Long
	suspend fun getThanksRecordPosition(thanksRecordId: Long): Int
	fun getThanksRecordsPagingFlow(): Flow<PagingData<ThanksRecordWithImages>>
}