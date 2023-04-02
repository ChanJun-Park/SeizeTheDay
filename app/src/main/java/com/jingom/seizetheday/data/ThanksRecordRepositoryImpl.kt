package com.jingom.seizetheday.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jingom.seizetheday.data.db.dao.AttachedImageEntityDao
import com.jingom.seizetheday.data.db.dao.ThanksRecordEntityDao
import com.jingom.seizetheday.data.db.model.toDBModel
import com.jingom.seizetheday.data.db.model.toDataModel
import com.jingom.seizetheday.domain.ThanksRecordRepository
import com.jingom.seizetheday.domain.model.AttachedImage
import com.jingom.seizetheday.domain.model.ThanksRecord
import com.jingom.seizetheday.domain.model.ThanksRecordWithImages
import kotlinx.coroutines.flow.Flow

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

	override fun getThanksRecordsPagingFlow(startThanksId: Long?): Flow<PagingData<ThanksRecordWithImages>> {
		return Pager(
			config = PagingConfig(
				pageSize = 15,
				initialLoadSize = 15
			),
			pagingSourceFactory = {
				ThanksRecordWithImagesPagingSource(
					thanksRecordEntityDao = thanksRecordEntityDao,
					attachedImageEntityDao = attachedImageEntityDao,
					startThanksId = startThanksId
				)
			}
		).flow
	}
}