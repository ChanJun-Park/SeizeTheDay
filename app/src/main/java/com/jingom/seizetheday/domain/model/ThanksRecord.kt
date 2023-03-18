package com.jingom.seizetheday.domain.model

import java.time.LocalDate

data class ThanksRecord(
	val id: Long,
	val feeling: Feeling,
	val thanksContent: String,
	val date: LocalDate
)
