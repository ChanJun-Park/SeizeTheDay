package com.jingom.seizetheday.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jingom.seizetheday.domain.model.ThanksRecord
import com.jingom.seizetheday.domain.usecase.GetThanksRecordsFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ListThanksViewModel @Inject constructor(
	private val getThanksRecordsFlow: GetThanksRecordsFlowUseCase
) : ViewModel() {

	private val _thanksRecords = MutableStateFlow(ListThanksScreenState(emptyList()))
	val thanksRecords: StateFlow<ListThanksScreenState> = _thanksRecords

	init {
		getThanksRecordsFlow()
			.onEach { updateThanksRecords(it) }
			.launchIn(viewModelScope)
	}

	private fun updateThanksRecords(it: List<ThanksRecord>) {
		_thanksRecords.value = thanksRecords.value.copy(thanksRecords = it)
	}
}