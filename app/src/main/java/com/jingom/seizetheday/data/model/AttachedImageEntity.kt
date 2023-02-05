package com.jingom.seizetheday.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity

private const val THANKS_RECORD_ID_COLUMN_NAME = "thanks_record_id"
private const val IMAGE_URL_COLUMN_NAME = "image_uri"

@Entity(
	tableName = "attached_image",
	primaryKeys = [THANKS_RECORD_ID_COLUMN_NAME, IMAGE_URL_COLUMN_NAME]
)
data class AttachedImageEntity(
	@ColumnInfo(name = THANKS_RECORD_ID_COLUMN_NAME)
	val thanksRecordId: Int,
	@ColumnInfo(name = IMAGE_URL_COLUMN_NAME)
	val imageUri: String
)
