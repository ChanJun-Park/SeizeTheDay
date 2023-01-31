package com.jingom.seizetheday.domain.usecase

import androidx.paging.PagingData
import com.jingom.seizetheday.domain.ThanksRecordRepository
import com.jingom.seizetheday.domain.model.ThanksRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThanksRecordsPagingDataFlowUseCase @Inject constructor(
	private val thanksRecordRepository: ThanksRecordRepository
) {
	operator fun invoke(): Flow<PagingData<ThanksRecord>> {
		return thanksRecordRepository.getThanksRecordsPagingFlow()
	}
}