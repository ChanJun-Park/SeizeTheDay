package com.jingom.seizetheday.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.jingom.seizetheday.di.coroutine.IoDispatcher
import com.jingom.seizetheday.domain.model.ThanksRecord
import com.jingom.seizetheday.domain.model.ThanksRecordsMap
import com.jingom.seizetheday.domain.usecase.GetThanksRecordsFlowUseCase
import com.jingom.seizetheday.domain.usecase.GetThanksRecordsPagingDataFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class ListThanksViewModel @Inject constructor(
	getThanksRecordsPagingDataFlow: GetThanksRecordsPagingDataFlowUseCase,
	@IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

	private val _thanksRecordsPagingData = MutableStateFlow<PagingData<ListThanksRecordUiModel>>(PagingData.empty())
	val thanksRecordsPagingData: StateFlow<PagingData<ListThanksRecordUiModel>> = _thanksRecordsPagingData

	init {
		viewModelScope.launch {
			getThanksRecordsPagingDataFlow().cachedIn(viewModelScope + ioDispatcher).collectLatest {
				val listThanksRecordUiModels = mapToUiModels(it)
				val headerInsertedListThanksRecordUiModels = insertDateSeparators(listThanksRecordUiModels)
				_thanksRecordsPagingData.update { headerInsertedListThanksRecordUiModels }
			}
		}
	}

	private fun mapToUiModels(it: PagingData<ThanksRecord>) =
		it.map { thanksRecord -> ListThanksRecordUiModel.ThanksRecordItem(thanksRecord) }

	private fun insertDateSeparators(listThanksRecordUiModels: PagingData<ListThanksRecordUiModel.ThanksRecordItem>) =
		listThanksRecordUiModels.insertSeparators { before: ListThanksRecordUiModel.ThanksRecordItem?, after: ListThanksRecordUiModel.ThanksRecordItem? ->
			if (after != null && (before == null || (before.thanksRecord.date != after.thanksRecord.date))) {
				ListThanksRecordUiModel.DateHeaderItem(after.thanksRecord.date)
			} else {
				null
			}
		}
}