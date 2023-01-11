package com.jingom.seizetheday.presentation.write

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jingom.seizetheday.domain.model.Feeling
import com.jingom.seizetheday.presentation.ui.theme.SeizeTheDayTheme
import kotlinx.coroutines.flow.collectLatest

enum class WritingThanksStep {
	EDITING,
	SAVING,
	SAVED,
	CANCELED
}

data class WritingThanksScreenState(
	val step: WritingThanksStep = WritingThanksStep.EDITING,
	val feeling: Feeling? = null,
	val content: String = "",
) {
	fun changeFeeling(feeling: Feeling): WritingThanksScreenState {
		return this.copy(feeling = feeling)
	}

	fun changeContent(content: String): WritingThanksScreenState {
		return this.copy(content = content)
	}
}

@Composable
fun WritingThanksScreen(
	viewModel: WritingThanksViewModel = hiltViewModel(),
	onWritingCancel: () -> Unit = {},
	onWritingDone: () -> Unit = {}
) {
	val navController = rememberNavController()
	val state = viewModel.writingThanksScreenState.collectAsState()

	LaunchedEffect(key1 = true) {
		viewModel.writingThanksScreenState.collectLatest {
			if (it.step == WritingThanksStep.CANCELED) {
				onWritingCancel()
			}
			if (it.step == WritingThanksStep.SAVED) {
				onWritingDone()
			}
		}
	}

	NavHost(
		navController = navController,
		startDestination = Route.SELECTING_FEELING
	) {
		composable(Route.SELECTING_FEELING) {
			SelectFeelingScreen(
				state = state.value,
				onFeelingSelected = { feeling ->
					viewModel.selectFeeling(feeling)
					navController.navigate(Route.WRITING_THANKS_CONTENT)
				}
			)
		}
		composable(Route.WRITING_THANKS_CONTENT) {
			WritingThanksContentScreen(
				state = state.value,
				onThanksContentChanged = viewModel::changeThanksContent,
				onSaveClick = viewModel::save
			)
		}
	}
}

@Preview
@Composable
fun WriteThanksScreenPreview() {
	SeizeTheDayTheme {
		WritingThanksScreen()
	}
}