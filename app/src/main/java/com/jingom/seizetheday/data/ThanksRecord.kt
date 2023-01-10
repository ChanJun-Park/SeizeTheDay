package com.jingom.seizetheday.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jingom.seizetheday.domain.Feeling

@Entity(tableName = "thanks_record")
data class ThanksRecord(
	@PrimaryKey(autoGenerate = true)
	@ColumnInfo(name = "id")
	val id: Int,
	@ColumnInfo(name = "feeling")
	val feeling: Feeling,
	@ColumnInfo(name = "thanks_content")
	val thanksContent: String
)
