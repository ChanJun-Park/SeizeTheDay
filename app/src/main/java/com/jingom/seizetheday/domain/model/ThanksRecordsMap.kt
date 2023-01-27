package com.jingom.seizetheday.domain.model

import java.time.LocalDate

data class ThanksRecordsMap(
	private val groupedThanksRecordByDate: Map<LocalDate, List<ThanksRecord>>
) {
	fun forEachDateThanksRecords(action: (LocalDate, List<ThanksRecord>) -> Unit) {
		groupedThanksRecordByDate.forEach(action)
	}
}