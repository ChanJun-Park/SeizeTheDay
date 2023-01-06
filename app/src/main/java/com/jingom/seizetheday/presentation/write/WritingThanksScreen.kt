package com.jingom.seizetheday.presentation.write

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jingom.seizetheday.domain.Feeling
import com.jingom.seizetheday.presentation.ui.theme.SeizeTheDayTheme

data class WritingThanksScreenState(
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
fun WritingThanksScreen(viewModel: WritingThanksViewModel = hiltViewModel()) {
	val navController = rememberNavController()
	val state = viewModel.writingThanksScreenState

	NavHost(
		navController = navController,
		startDestination = Route.SELECTING_FEELING
	) {
		composable(Route.SELECTING_FEELING) {
			SelectFeelingScreen(
				state = state,
				onFeelingSelected = { feeling ->
					viewModel.selectFeeling(feeling)
					navController.navigate(Route.WRITING_THANKS_CONTENT)
				}
			)
		}
		composable(Route.WRITING_THANKS_CONTENT) {
			WritingThanksContentScreen(
				state = state,
				onThanksContentChanged = viewModel::changeThanksContent
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