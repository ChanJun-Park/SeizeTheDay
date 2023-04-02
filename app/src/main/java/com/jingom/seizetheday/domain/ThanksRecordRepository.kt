package com.jingom.seizetheday.domain

import androidx.paging.PagingData
import com.jingom.seizetheday.domain.model.AttachedImage
import com.jingom.seizetheday.domain.model.ThanksRecord
import com.jingom.seizetheday.domain.model.ThanksRecordWithImages
import kotlinx.coroutines.flow.Flow

interface ThanksRecordRepository {
	suspend fun saveThanksRecord(thanksRecord: ThanksRecord): Long
	suspend fun saveAttachedImages(attachedImages: List<AttachedImage>)
	fun getThanksRecordsPagingFlow(startThanksId: Long?): Flow<PagingData<ThanksRecordWithImages>>
}