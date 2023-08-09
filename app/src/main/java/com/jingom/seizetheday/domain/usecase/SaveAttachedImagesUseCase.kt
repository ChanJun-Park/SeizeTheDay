package com.jingom.seizetheday.domain.usecase

import com.jingom.seizetheday.domain.AttachedImageRepository
import com.jingom.seizetheday.domain.ThanksRecordRepository
import com.jingom.seizetheday.domain.model.AttachedImage
import javax.inject.Inject

class SaveAttachedImagesUseCase @Inject constructor(
	private val attachedImageRepository: AttachedImageRepository
) {
	suspend operator fun invoke(attachedImages: List<AttachedImage>) = runCatching {
		attachedImageRepository.saveAttachedImages(attachedImages)
	}
}