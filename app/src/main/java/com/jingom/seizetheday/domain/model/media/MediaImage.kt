package com.jingom.seizetheday.domain.model.media

import android.net.Uri

data class MediaImage(
	val id: Long,
	val albumId: Int,
	val contentUri: Uri,
	val mimeType: String,
	val dateModified: Long,
	val orientation: Int
)
