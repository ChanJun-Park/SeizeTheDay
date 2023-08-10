package com.jingom.seizetheday.data.db.converter

import java.time.LocalDate

interface DateTypeConverters {
	fun toDateString(date: LocalDate): String
	fun toDate(dateString: String): LocalDate
}