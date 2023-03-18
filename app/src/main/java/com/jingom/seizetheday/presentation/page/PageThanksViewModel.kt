package com.jingom.seizetheday.presentation.page

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jingom.seizetheday.di.coroutine.IoDispatcher
import com.jingom.seizetheday.domain.model.ThanksRecord
import com.jingom.seizetheday.domain.usecase.GetThanksRecordsPagingDataFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class PageThanksViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	getThanksRecordsPagingDataFlow: GetThanksRecordsPagingDataFlowUseCase,
	@IoDispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel() {
	private val _thanksRecordsPagingData = MutableStateFlow<PagingData<ThanksRecord>>(PagingData.empty())
	val thanksRecordsPagingData: StateFlow<PagingData<ThanksRecord>> = _thanksRecordsPagingData

	init {
		val startThanksIdString: String? = savedStateHandle["startThanksId"]
		val startThanksId = startThanksIdString?.toLongOrNull()

		viewModelScope.launch {
			getThanksRecordsPagingDataFlow(startThanksId).cachedIn(viewModelScope + ioDispatcher).collectLatest {
				_thanksRecordsPagingData.value = it
			}
		}
	}
}