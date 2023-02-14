package com.jingom.seizetheday.data.db.converter

import androidx.room.TypeConverter
import java.time.LocalDate

class DateTypeConverters {
	@TypeConverter
	fun toDateString(date: LocalDate): String = date.toString()

	@TypeConverter
	fun toDate(dateString: String): LocalDate = LocalDate.parse(dateString)
}