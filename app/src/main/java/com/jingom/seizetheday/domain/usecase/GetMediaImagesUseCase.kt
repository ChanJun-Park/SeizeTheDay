package com.jingom.seizetheday.domain.usecase

import com.jingom.seizetheday.domain.LocalMediaImageLoader
import com.jingom.seizetheday.domain.LocalMediaImageLoader.Companion.ALL_IMAGES_ALBUM_ID
import com.jingom.seizetheday.domain.model.media.MediaImage
import javax.inject.Inject

class GetMediaImagesUseCase @Inject constructor(
	private val localMediaImageLoader: LocalMediaImageLoader
) {
	suspend operator fun invoke(
		bucketId: Int = ALL_IMAGES_ALBUM_ID
	): List<MediaImage> {
		return localMediaImageLoader.getLocalMediaImageList(bucketId)
	}
}