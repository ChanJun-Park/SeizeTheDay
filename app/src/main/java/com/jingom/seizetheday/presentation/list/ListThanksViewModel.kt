package com.jingom.seizetheday.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.jingom.seizetheday.di.coroutine.IoDispatcher
import com.jingom.seizetheday.domain.model.ThanksRecordWithImages
import com.jingom.seizetheday.domain.usecase.GetThanksRecordWithImagesPagingDataFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class ListThanksViewModel @Inject constructor(
	getThanksRecordWitImagesPagingDataFlow: GetThanksRecordWithImagesPagingDataFlowUseCase
) : ViewModel() {

	val thanksRecordsPagingData = getThanksRecordWitImagesPagingDataFlow()
		.map { pagingData -> mapToUiModels(pagingData) }
		.cachedIn(viewModelScope)
		.map { pagingData -> insertDateSeparators(pagingData) }

	private fun mapToUiModels(pagingData: PagingData<ThanksRecordWithImages>) =
		pagingData.map { thanksRecord ->
			ListThanksRecordUiState.ThanksRecordItemWithImages(thanksRecord)
		}

	private fun insertDateSeparators(listThanksRecordUiModels: PagingData<ListThanksRecordUiState.ThanksRecordItemWithImages>) =
		listThanksRecordUiModels.insertSeparators { before: ListThanksRecordUiState.ThanksRecordItemWithImages?, after: ListThanksRecordUiState.ThanksRecordItemWithImages? ->
			createDateHeaderSeparatorsIfNeedTo(after, before)
		}

	private fun createDateHeaderSeparatorsIfNeedTo(
		after: ListThanksRecordUiState.ThanksRecordItemWithImages?,
		before: ListThanksRecordUiState.ThanksRecordItemWithImages?
	): ListThanksRecordUiState.DateHeaderItem? {
		after ?: return null
		before ?: return ListThanksRecordUiState.DateHeaderItem(after.thanksRecordWithImages.thanksRecord.date)

		return if (before.thanksRecordWithImages.thanksRecord.date != after.thanksRecordWithImages.thanksRecord.date) {
			ListThanksRecordUiState.DateHeaderItem(after.thanksRecordWithImages.thanksRecord.date)
		} else {
			null
		}
	}
}