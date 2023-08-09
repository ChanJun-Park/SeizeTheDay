package com.jingom.seizetheday.data

import com.jingom.seizetheday.data.db.dao.AttachedImageEntityDao
import com.jingom.seizetheday.data.db.model.toDataModel
import com.jingom.seizetheday.domain.AttachedImageRepository
import com.jingom.seizetheday.domain.model.AttachedImage

class AttachedImageRepositoryImpl(
	private val attachedImageEntityDao: AttachedImageEntityDao
): AttachedImageRepository {
	override suspend fun saveAttachedImages(attachedImages: List<AttachedImage>) {
		attachedImageEntityDao.insert(*attachedImages.map { it.toDataModel() }.toTypedArray())
	}
}