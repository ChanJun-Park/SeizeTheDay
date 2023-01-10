package com.jingom.seizetheday.data

import com.jingom.seizetheday.data.dao.ThanksRecordEntityDao
import com.jingom.seizetheday.data.model.toDBModel
import com.jingom.seizetheday.domain.ThanksRecordRepository
import com.jingom.seizetheday.domain.model.ThanksRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThanksRecordRepositoryImpl constructor(
	private val thanksRecordEntityDao: ThanksRecordEntityDao
): ThanksRecordRepository {
	override suspend fun saveThanksRecord(thanksRecord: ThanksRecord) {
		thanksRecordEntityDao.insert(thanksRecord = thanksRecord.toDBModel())
	}

	override fun getThanksRecordsFlow(): Flow<List<ThanksRecord>> {
		return thanksRecordEntityDao.getThanksRecordEntitiesFlow().map { list ->
			list.map { it.toDomainModel() }
		}
	}
}