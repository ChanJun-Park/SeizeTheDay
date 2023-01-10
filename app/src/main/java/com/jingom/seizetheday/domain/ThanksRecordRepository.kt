package com.jingom.seizetheday.domain

import com.jingom.seizetheday.domain.model.ThanksRecord
import kotlinx.coroutines.flow.Flow

interface ThanksRecordRepository {
	suspend fun saveThanksRecord(thanksRecord: ThanksRecord)
	fun getThanksRecordsFlow(): Flow<List<ThanksRecord>>
}