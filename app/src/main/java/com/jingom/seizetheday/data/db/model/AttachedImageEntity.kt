package com.jingom.seizetheday.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jingom.seizetheday.domain.model.AttachedImage

private const val THANKS_RECORD_ID_COLUMN_NAME = "thanks_record_id"
private const val IMAGE_URL_COLUMN_NAME = "image_uri"

@Entity(tableName = "attached_image_entity")
data class AttachedImageEntity(
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	val id: Long,
	@ColumnInfo(name = THANKS_RECORD_ID_COLUMN_NAME)
	val thanksRecordId: Long,
	@ColumnInfo(name = IMAGE_URL_COLUMN_NAME)
	val imageUri: String
) {
	fun toDomainModel(): AttachedImage = AttachedImage(id, thanksRecordId, imageUri)
}

fun AttachedImage.toDataModel(): AttachedImageEntity = AttachedImageEntity(id, thanksRecordId, imageUri)