package com.jingom.seizetheday.presentation.page

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jingom.seizetheday.di.coroutine.IoDispatcher
import com.jingom.seizetheday.domain.model.ThanksRecordWithImages
import com.jingom.seizetheday.domain.usecase.GetThanksRecordWithImagesPagingDataFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class PageThanksViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	getThanksRecordWithImagesPagingDataFlow: GetThanksRecordWithImagesPagingDataFlowUseCase,
	@IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
	private val _thanksRecordWithImagesPagingData = MutableStateFlow<PagingData<ThanksRecordWithImages>>(PagingData.empty())
	val thanksRecordWithImagesPagingData = _thanksRecordWithImagesPagingData.asStateFlow()

	init {
		val startThanksIdString: String? = savedStateHandle["startThanksId"]
		val startThanksId = startThanksIdString?.toLongOrNull()

		viewModelScope.launch {
			getThanksRecordWithImagesPagingDataFlow(startThanksId)
				.cachedIn(viewModelScope + ioDispatcher)
				.collectLatest {
					_thanksRecordWithImagesPagingData.value = it
				}
		}
	}
}