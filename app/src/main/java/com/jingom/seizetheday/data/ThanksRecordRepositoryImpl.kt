package com.jingom.seizetheday.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jingom.seizetheday.data.db.converter.DateTypeConverters
import com.jingom.seizetheday.data.db.dao.ThanksRecordEntityDao
import com.jingom.seizetheday.data.db.model.toDBModel
import com.jingom.seizetheday.domain.ThanksRecordRepository
import com.jingom.seizetheday.domain.model.ThanksRecord
import com.jingom.seizetheday.domain.model.ThanksRecordWithImages
import kotlinx.coroutines.flow.Flow

class ThanksRecordRepositoryImpl constructor(
	private val thanksRecordEntityDao: ThanksRecordEntityDao,
	private val dateTypeConverters: DateTypeConverters
): ThanksRecordRepository {
	override suspend fun saveThanksRecord(thanksRecord: ThanksRecord): Long {
		return thanksRecordEntityDao.insert(thanksRecord = thanksRecord.toDBModel())
	}

	override suspend fun getThanksRecordPosition(thanksRecordId: Long): Int {
		val thanksRecord = thanksRecordEntityDao.getThanksRecord(thanksRecordId)
		return thanksRecordEntityDao.getThanksRecordRowIndex(thanksRecordId, dateTypeConverters.toDateString(thanksRecord.date))
	}

	override fun getThanksRecordsPagingFlow(): Flow<PagingData<ThanksRecordWithImages>> {
		return Pager(
			config = PagingConfig(
				pageSize = 15,
				initialLoadSize = 15
			),
			pagingSourceFactory = {
				ThanksRecordWithImagesPagingSource(thanksRecordEntityDao)
			}
		).flow
	}
}