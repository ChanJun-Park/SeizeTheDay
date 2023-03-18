package com.jingom.seizetheday.presentation.write

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jingom.seizetheday.core.ui.SelectedImageList
import com.jingom.seizetheday.domain.model.AttachedImage
import com.jingom.seizetheday.domain.model.Feeling
import com.jingom.seizetheday.domain.model.ThanksRecord
import com.jingom.seizetheday.domain.model.media.MediaImage
import com.jingom.seizetheday.domain.usecase.SaveAttachedImagesUseCase
import com.jingom.seizetheday.domain.usecase.SaveThanksRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WritingThanksViewModel @Inject constructor(
	private val saveThanksRecord: SaveThanksRecordUseCase,
	private val saveAttachedImages: SaveAttachedImagesUseCase
) : ViewModel() {

	private val _writingThanksScreenState = MutableStateFlow(WritingThanksScreenState())
	val writingThanksScreenState = _writingThanksScreenState.asStateFlow()

	private val _attachedImageListState = MutableStateFlow<List<AttachedImage>>(emptyList())
	val attachedImageListState = _attachedImageListState.asStateFlow()

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

			val result = saveThanksRecord(thanksRecord)
			val thanksRecordId = result.getOrNull()
			if (result.isFailure || thanksRecordId == null) {
				restorePrevState(prevState)
				return@launch
			}

			val attachedImages = attachedImageListState.value.map {
				it.copy(thanksRecordId = thanksRecordId)
			}

			saveAttachedImages(attachedImages)
				.onSuccess {
					changeWritingThanksStepToSaved()
				}
				.onFailure {
					changeWritingThanksStepToSaved()
				}
		}
	}

	fun attachSelectedImages(result: SelectedImageList) {
		val newAttachedImages = result.map { it.toAttachedImage() }
		_attachedImageListState.value = attachedImageListState.value + newAttachedImages
	}

	private fun MediaImage.toAttachedImage() = AttachedImage(
		id = 0,
		thanksRecordId = 0,
		imageUri = contentUri.toString()
	)

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