package com.jingom.seizetheday.presentation.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jingom.seizetheday.domain.model.ThanksRecord
import com.jingom.seizetheday.domain.usecase.GetThanksRecordsPagingDataFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PageThanksViewModel @Inject constructor(
	getThanksRecordsPagingDataFlow: GetThanksRecordsPagingDataFlowUseCase
): ViewModel() {
	private val _thanksRecordsPagingData = MutableStateFlow<PagingData<ThanksRecord>>(PagingData.empty())
	val thanksRecordsPagingData: StateFlow<PagingData<ThanksRecord>> = _thanksRecordsPagingData

	init {
		viewModelScope.launch {
			getThanksRecordsPagingDataFlow().cachedIn(viewModelScope).collectLatest {
				_thanksRecordsPagingData.value = it
			}
		}
	}
}