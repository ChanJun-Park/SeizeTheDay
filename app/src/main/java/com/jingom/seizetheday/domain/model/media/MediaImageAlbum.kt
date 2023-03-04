package com.jingom.seizetheday.domain.model.media

data class MediaImageAlbum(
	val albumId: Int,
	val albumName: String = "",
	val thumbnailImage: MediaImage? = null,
	val imageCount: Int = 0
)
