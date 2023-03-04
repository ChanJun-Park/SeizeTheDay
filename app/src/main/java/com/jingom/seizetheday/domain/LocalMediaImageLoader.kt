package com.jingom.seizetheday.domain

import com.jingom.seizetheday.domain.model.media.MediaImage
import com.jingom.seizetheday.domain.model.media.MediaImageAlbum

interface LocalMediaImageLoader {
	suspend fun getLocalMedialImageAlbumList(): List<MediaImageAlbum>
	suspend fun getLocalMediaImageList(albumId: Int = ALL_IMAGES_ALBUM_ID): List<MediaImage>

	companion object {
		const val ALL_IMAGES_ALBUM_ID = -1
	}
}