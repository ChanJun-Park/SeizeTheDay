package com.jingom.seizetheday.presentation.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jingom.seizetheday.di.coroutine.IoDispatcher
import com.jingom.seizetheday.domain.model.ThanksRecordWithImages
import com.jingom.seizetheday.domain.usecase.GetThanksRecordPositionUseCase
import com.jingom.seizetheday.domain.usecase.GetThanksRecordWithImagesPagingDataFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class PageThanksViewModel @Inject constructor(
	private val getThanksRecordWithImagesPagingDataFlow: GetThanksRecordWithImagesPagingDataFlowUseCase,
	private val getThanksRecordPosition: GetThanksRecordPositionUseCase,
	@IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
	private var initialized = false

	private val _thanksRecordWithImagesPagingData = MutableStateFlow<PagingData<ThanksRecordWithImages>>(PagingData.empty())
	val thanksRecordWithImagesPagingData = _thanksRecordWithImagesPagingData.asStateFlow()

	private val _startIndex = MutableStateFlow(INVALID_START_INDEX)
	val startIndex = _startIndex.asStateFlow()

	fun init(startThanksId: Long?) {
		if (initialized) {
			return
		}

		viewModelScope.launch {
			getThanksRecordWithImagesPagingDataFlow()
				.cachedIn(viewModelScope + ioDispatcher)
				.collectLatest {
					_thanksRecordWithImagesPagingData.value = it

					initializeStartIndex(startThanksId)
				}
		}

		initialized = true
	}

	private suspend fun initializeStartIndex(startThanksId: Long?) {
		if (_startIndex.value != INVALID_START_INDEX) {
			return
		}

		_startIndex.value = getStartIndex(startThanksId)
	}

	private suspend fun getStartIndex(startThanksId: Long?) = if (startThanksId == null) {
		0
	} else {
		getThanksRecordPosition(startThanksId)
	}

	companion object {
		const val INVALID_START_INDEX = -1
	}
}