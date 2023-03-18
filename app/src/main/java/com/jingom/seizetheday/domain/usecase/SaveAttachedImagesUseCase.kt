package com.jingom.seizetheday.domain.usecase

import com.jingom.seizetheday.domain.ThanksRecordRepository
import com.jingom.seizetheday.domain.model.AttachedImage
import javax.inject.Inject

class SaveAttachedImagesUseCase @Inject constructor(
	private val thanksRecordRepository: ThanksRecordRepository
) {
	suspend operator fun invoke(attachedImages: List<AttachedImage>) = runCatching {
		thanksRecordRepository.saveAttachedImages(attachedImages)
	}
}