package com.jingom.seizetheday.domain.usecase

import com.jingom.seizetheday.domain.ThanksRecordRepository
import com.jingom.seizetheday.domain.model.ThanksRecord
import javax.inject.Inject

class SaveThanksRecordUseCase @Inject constructor(
	private val thanksRecordRepository: ThanksRecordRepository
) {
	suspend operator fun invoke(thanksRecord: ThanksRecord) {
		thanksRecordRepository.saveThanksRecord(thanksRecord)
	}
}