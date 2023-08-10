package com.jingom.seizetheday.domain.usecase

import androidx.paging.PagingData
import com.jingom.seizetheday.domain.ThanksRecordRepository
import com.jingom.seizetheday.domain.model.ThanksRecordWithImages
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThanksRecordWithImagesPagingDataFlowUseCase @Inject constructor(
	private val thanksRecordRepository: ThanksRecordRepository
) {
	operator fun invoke(): Flow<PagingData<ThanksRecordWithImages>> {
		return thanksRecordRepository.getThanksRecordsPagingFlow()
	}
}