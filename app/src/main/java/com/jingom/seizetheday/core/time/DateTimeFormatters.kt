package com.jingom.seizetheday.core.time

import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object DateTimeFormatters {
	val localizedDate: DateTimeFormatter
		get() = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
}