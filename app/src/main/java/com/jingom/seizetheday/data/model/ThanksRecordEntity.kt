package com.jingom.seizetheday.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jingom.seizetheday.domain.model.Feeling
import com.jingom.seizetheday.domain.model.ThanksRecord

@Entity(tableName = "thanks_record_entity")
data class ThanksRecordEntity(
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	val id: Int,
	@ColumnInfo(name = "feeling")
	val feeling: Feeling,
	@ColumnInfo(name = "thanks_content")
	val thanksContent: String
) {
	fun toDomainModel(): ThanksRecord = ThanksRecord(
		id = id,
		feeling = feeling,
		thanksContent = thanksContent
	)
}

fun ThanksRecord.toDBModel(): ThanksRecordEntity = ThanksRecordEntity(
	id = id,
	feeling = feeling,
	thanksContent = thanksContent
)
