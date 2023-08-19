package com.jingom.seizetheday.core.time

import org.junit.Test
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

class DateTimeFormatTest {
	@Test
	fun ofLocalizedDateTest() {
		val date = LocalDate.of(2023, 8, 19)
		var dateString = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL))
		println(dateString)

		dateString = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).localizedBy(Locale.ENGLISH))
		println(dateString)
	}
}