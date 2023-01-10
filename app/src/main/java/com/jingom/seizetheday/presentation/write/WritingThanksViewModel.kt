package com.jingom.seizetheday.presentation.write

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jingom.seizetheday.domain.model.Feeling
import com.jingom.seizetheday.domain.model.ThanksRecord
import com.jingom.seizetheday.domain.usecase.SaveThanksRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WritingThanksViewModel @Inject constructor(
	private val saveThanksRecordUseCase: SaveThanksRecordUseCase
) : ViewModel() {

	var writingThanksScreenState by mutableStateOf(WritingThanksScreenState())

	fun selectFeeling(feeling: Feeling) {
		writingThanksScreenState = writingThanksScreenState
			.changeFeeling(feeling)
	}

	fun changeThanksContent(content: String) {
		writingThanksScreenState = writingThanksScreenState
			.changeContent(content = content)
	}

	fun save() {
		val selectedFeeling = writingThanksScreenState.feeling ?: return
		val content = writingThanksScreenState.content

		val thanksRecord = ThanksRecord(
			id = 0,
			feeling = selectedFeeling,
			thanksContent = content
		)

		viewModelScope.launch {
			saveThanksRecordUseCase(thanksRecord)
		}
	}
}