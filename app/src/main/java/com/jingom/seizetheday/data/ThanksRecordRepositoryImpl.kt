package com.jingom.seizetheday.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.jingom.seizetheday.data.db.dao.AttachedImageEntityDao
import com.jingom.seizetheday.data.db.dao.ThanksRecordEntityDao
import com.jingom.seizetheday.data.db.model.toDBModel
import com.jingom.seizetheday.data.db.model.toDataModel
import com.jingom.seizetheday.domain.ThanksRecordRepository
import com.jingom.seizetheday.domain.model.AttachedImage
import com.jingom.seizetheday.domain.model.ThanksRecord
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThanksRecordRepositoryImpl constructor(
	private val thanksRecordEntityDao: ThanksRecordEntityDao,
	private val attachedImageEntityDao: AttachedImageEntityDao
): ThanksRecordRepository {
	override suspend fun saveThanksRecord(thanksRecord: ThanksRecord): Long {
		return thanksRecordEntityDao.insert(thanksRecord = thanksRecord.toDBModel())
	}

	override suspend fun saveAttachedImages(attachedImages: List<AttachedImage>) {
		attachedImageEntityDao.insert(*attachedImages.map { it.toDataModel() }.toTypedArray())
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