package com.jingom.seizetheday.data.db.model

import androidx.room.Embedded
import androidx.room.Relation
import com.jingom.seizetheday.domain.model.AttachedImageList
import com.jingom.seizetheday.domain.model.ThanksRecordWithImages

data class ThanksRecordWithAttachedImagesEntity(
	@Embedded val thanksRecordEntity: ThanksRecordEntity,
	@Relation(
		parentColumn = "id",
		entityColumn = AttachedImageEntity.THANKS_RECORD_ID_COLUMN_NAME
	)
	val attachedImageEntities: List<AttachedImageEntity>
)

fun List<AttachedImageEntity>.toAttachedImageList() = AttachedImageList(this.map { it.toDomainModel() })

fun ThanksRecordWithAttachedImagesEntity.toDomainModel() = ThanksRecordWithImages(
	thanksRecord = this.thanksRecordEntity.toDomainModel(),
	attachedImageList = this.attachedImageEntities.toAttachedImageList()
)