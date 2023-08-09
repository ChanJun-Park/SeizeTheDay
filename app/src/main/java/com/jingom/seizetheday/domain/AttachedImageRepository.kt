package com.jingom.seizetheday.domain

import com.jingom.seizetheday.domain.model.AttachedImage

interface AttachedImageRepository {
	suspend fun saveAttachedImages(attachedImages: List<AttachedImage>)
}