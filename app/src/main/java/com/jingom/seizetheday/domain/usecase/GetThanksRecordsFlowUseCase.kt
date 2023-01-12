package com.jingom.seizetheday.domain.usecase

import com.jingom.seizetheday.domain.ThanksRecordRepository
import com.jingom.seizetheday.domain.model.ThanksRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThanksRecordsFlowUseCase @Inject constructor(
	private val thanksRecordRepository: ThanksRecordRepository
) {
	operator fun invoke(): Flow<List<ThanksRecord>> {
		return thanksRecordRepository.getThanksRecordsFlow()
	}
}