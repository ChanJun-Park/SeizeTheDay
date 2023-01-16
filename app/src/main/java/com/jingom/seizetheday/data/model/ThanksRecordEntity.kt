package com.jingom.seizetheday.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jingom.seizetheday.domain.model.Feeling
import com.jingom.seizetheday.domain.model.ThanksRecord
import java.time.LocalDate

@Entity(tableName = "thanks_record_entity")
data class ThanksRecordEntity(
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	val id: Int,
	@ColumnInfo(name = "feeling")
	val feeling: Feeling,
	@ColumnInfo(name = "thanks_content")
	val thanksContent: String,
	@ColumnInfo(name = "date")
	val date: LocalDate
) {
	fun toDomainModel(): ThanksRecord = ThanksRecord(
		id = id,
		feeling = feeling,
		thanksContent = thanksContent,
		date = date
	)
}

fun ThanksRecord.toDBModel(): ThanksRecordEntity = ThanksRecordEntity(
	id = id,
	feeling = feeling,
	thanksContent = thanksContent,
	date
)
