package com.jingom.seizetheday.domain.usecase

import com.jingom.seizetheday.domain.LocalMediaImageLoader
import com.jingom.seizetheday.domain.model.media.MediaImageAlbum
import javax.inject.Inject

class GetMediaImageAlbumsUseCase @Inject constructor(
	private val localMediaImageLoader: LocalMediaImageLoader
) {
	suspend operator fun invoke(): List<MediaImageAlbum> {
		return localMediaImageLoader.getLocalMedialImageAlbumList()
	}
}