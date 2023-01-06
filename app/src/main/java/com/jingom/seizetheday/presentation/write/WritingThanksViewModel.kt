package com.jingom.seizetheday.presentation.write

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.jingom.seizetheday.domain.Feeling
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WritingThanksViewModel @Inject constructor() : ViewModel() {

	var writingThanksScreenState by mutableStateOf(WritingThanksScreenState())

	fun selectFeeling(feeling: Feeling) {
		writingThanksScreenState = writingThanksScreenState
			.changeFeeling(feeling)
	}

	fun changeThanksContent(content: String) {
		writingThanksScreenState = writingThanksScreenState
			.changeContent(content = content)
	}
}