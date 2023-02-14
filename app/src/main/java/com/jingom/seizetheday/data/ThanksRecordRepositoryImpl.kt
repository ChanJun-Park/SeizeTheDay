package com.jingom.seizetheday.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.jingom.seizetheday.data.db.dao.ThanksRecordEntityDao
import com.jingom.seizetheday.data.db.model.toDBModel
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

	override fun getThanksRecordsPagingFlow(startThanksId: Int?): Flow<PagingData<ThanksRecord>> {
		return Pager(
			config = PagingConfig(
				pageSize = 15,
				initialLoadSize = 15
			),
			pagingSourceFactory = {
				ThanksPageSource(
					thanksRecordEntityDao = thanksRecordEntityDao,
					startThanksId = startThanksId
				)
			}
		).flow.map { pagingData -> pagingData.map { it.toDomainModel() } }
	}
}