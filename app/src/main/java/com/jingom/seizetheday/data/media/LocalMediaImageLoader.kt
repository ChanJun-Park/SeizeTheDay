package com.jingom.seizetheday.data.media

import com.jingom.seizetheday.data.media.model.MediaImage
import com.jingom.seizetheday.data.media.model.MediaImageAlbum

interface LocalMediaImageLoader {
	suspend fun getLocalMedialImageAlbumList(): List<MediaImageAlbum>
	suspend fun getLocalMediaImageList(bucketId: Int = LocalMediaImageCursorLoader.ALL_IMAGES_BUCKET_ID): List<MediaImage>
}