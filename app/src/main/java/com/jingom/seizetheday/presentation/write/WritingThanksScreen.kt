package com.jingom.seizetheday.presentation.write

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jingom.seizetheday.domain.Feeling
import com.jingom.seizetheday.presentation.ui.theme.SeizeTheDayTheme

enum class WritingThanksStep {
	SELECT_FEELING,
	WRITING_THANKS_CONTENT
}

data class WritingThanksScreenState(
	val feeling: Feeling? = null,
	val content: String = "",
	val step: WritingThanksStep = WritingThanksStep.SELECT_FEELING
) {
	fun isSelectFeelingStep() = step == WritingThanksStep.SELECT_FEELING
	fun isWritingContentStepForSelectedFeeling() = step == WritingThanksStep.WRITING_THANKS_CONTENT

	fun changeFeeling(feeling: Feeling): WritingThanksScreenState {
		return this.copy(feeling = feeling)
	}

	fun changeContent(content: String): WritingThanksScreenState {
		return this.copy(content = content)
	}

	fun changeStep(step: WritingThanksStep): WritingThanksScreenState {
		return this.copy(step = step)
	}
}

@Composable
fun WritingThanksScreen(
	viewModel: WritingThanksViewModel = hiltViewModel()
) {
	WritingThanksScreenScrollContainer {
		WritingThanksBody(viewModel = viewModel)
	}
}

@Composable
fun WritingThanksScreenScrollContainer(
	bodyContent: @Composable () -> Unit
) {
	Surface(modifier = Modifier.fillMaxSize()) {
		val verticalScrollState = rememberScrollState()
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(
					state = verticalScrollState,
					flingBehavior = ScrollableDefaults.flingBehavior()
				)
		) {
			bodyContent()
		}
	}
}

@Composable
fun WritingThanksBody(viewModel: WritingThanksViewModel) {
	val state = viewModel.writingThanksScreenState

	Column(
		modifier = Modifier
			.wrapContentHeight()
			.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Top
	) {
		if (state.isWritingContentStepForSelectedFeeling() && state.feeling != null) {
			SelectedFeeling(feeling = state.feeling)
		}

		if (state.isSelectFeelingStep()) {
			SelectFeelingPromptMessage()
			FeelingSelectSection(onFeelingSelected = viewModel::selectFeeling)
		} else {
			WriteThanksContentPromptMessage()
			ContentForSelectedFeeling(state.content, viewModel::changeThanksContent)
		}
	}
}

@Composable
fun SelectedFeeling(feeling: Feeling) {
	Text(
		text = feeling.name,
		style = MaterialTheme.typography.h2
	)
}

@Composable
fun FeelingItem(
	feeling: Feeling,
	onItemClick: (Feeling) -> Unit
) {
	Box(
		modifier = Modifier
			.clickable { onItemClick(feeling) }
			.padding(10.dp)
	) {
		Text(
			text = feeling.name,
			style = MaterialTheme.typography.body1
		)
	}
}

@Preview
@Composable
fun SelectedFeelingPreview() {
	SelectedFeeling(feeling = Feeling.Happy)
}

@Composable
fun SelectFeelingPromptMessage() {
	Text(text = "지금 기분이 어떠신가요?")
}

@Preview
@Composable
fun SelectFeelingPromptMessagePreview() {
	SelectFeelingPromptMessage()
}

@Composable
fun WriteThanksContentPromptMessage() {
	Text(text = "감사한 내용을 적어보세요")
}

@Preview
@Composable
fun WriteThanksContentPromptMessagePreview() {
	WriteThanksContentPromptMessage()
}

@Composable
fun FeelingSelectSection(onFeelingSelected: (Feeling) -> Unit) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.wrapContentHeight(),
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Row(
			modifier = Modifier.wrapContentSize()
		) {
			FeelingItem(feeling = Feeling.Happy, onItemClick = onFeelingSelected)
			FeelingItem(feeling = Feeling.Joy, onItemClick = onFeelingSelected)
			FeelingItem(feeling = Feeling.Hope, onItemClick = onFeelingSelected)
		}
		Row(
			modifier = Modifier.wrapContentSize()
		) {
			FeelingItem(feeling = Feeling.Thanks, onItemClick = onFeelingSelected)
			FeelingItem(feeling = Feeling.Pride, onItemClick = onFeelingSelected)
			FeelingItem(feeling = Feeling.Serenity, onItemClick = onFeelingSelected)
			FeelingItem(feeling = Feeling.Awe, onItemClick = onFeelingSelected)
		}
	}
}

@Composable
fun ContentForSelectedFeeling(
	content: String,
	onContentChanged: (String) -> Unit
) {
	TextField(value = content, onValueChange = onContentChanged)
}

@Preview
@Composable
fun WriteThanksScreenPreview() {
	SeizeTheDayTheme {
		WritingThanksScreen()
	}
}