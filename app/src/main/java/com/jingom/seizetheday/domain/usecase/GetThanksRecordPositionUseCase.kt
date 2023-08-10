package com.jingom.seizetheday.domain.usecase

import com.jingom.seizetheday.domain.ThanksRecordRepository
import javax.inject.Inject

class GetThanksRecordPositionUseCase @Inject constructor(
	private val thanksRecordRepository: ThanksRecordRepository
) {
	suspend operator fun invoke(thanksRecordId: Long): Int {
		return thanksRecordRepository.getThanksRecordPosition(thanksRecordId)
	}
}