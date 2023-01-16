package com.jingom.seizetheday.presentation.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jingom.seizetheday.domain.model.Feeling
import com.jingom.seizetheday.domain.model.ThanksRecord
import com.jingom.seizetheday.domain.usecase.SaveThanksRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WritingThanksViewModel @Inject constructor(
	private val saveThanksRecordUseCase: SaveThanksRecordUseCase
) : ViewModel() {

	private val _writingThanksScreenState = MutableStateFlow(WritingThanksScreenState())
	val writingThanksScreenState: StateFlow<WritingThanksScreenState> = _writingThanksScreenState

	fun selectFeeling(feeling: Feeling) {
		_writingThanksScreenState.value = writingThanksScreenState.value.changeFeeling(feeling)
	}

	fun changeThanksContent(content: String) {
		_writingThanksScreenState.value = writingThanksScreenState.value.changeContent(content = content)
	}

	fun save() {
		viewModelScope.launch {
			val prevState = writingThanksScreenState.value

			changeWritingThanksStepToSaving()

			val selectedFeeling = writingThanksScreenState.value.feeling ?: return@launch
			val content = writingThanksScreenState.value.content

			val thanksRecord = ThanksRecord(
				id = 0,
				feeling = selectedFeeling,
				thanksContent = content,
				date = LocalDate.now()
			)

			saveThanksRecordUseCase(thanksRecord).onSuccess {
				changeWritingThanksStepToSaved()
			}.onFailure {
				restorePrevState(prevState)
			}
		}
	}

	private fun changeWritingThanksStepToSaving() {
		_writingThanksScreenState.value = writingThanksScreenState.value.copy(
			step = WritingThanksStep.SAVING
		)
	}

	private fun changeWritingThanksStepToSaved() {
		_writingThanksScreenState.value = writingThanksScreenState.value.copy(
			step = WritingThanksStep.SAVED
		)
	}

	private fun restorePrevState(prevState: WritingThanksScreenState) {
		_writingThanksScreenState.value = prevState
	}
}