package com.jingom.seizetheday.data.db.converter

import androidx.room.TypeConverter
import java.time.LocalDate

class DateTypeConvertersImpl: DateTypeConverters {
	@TypeConverter
	override fun toDateString(date: LocalDate): String = date.toString()

	@TypeConverter
	override fun toDate(dateString: String): LocalDate = LocalDate.parse(dateString)
}