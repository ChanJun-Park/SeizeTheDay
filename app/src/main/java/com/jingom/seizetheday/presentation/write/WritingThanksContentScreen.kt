package com.jingom.seizetheday.presentation.write

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jingom.seizetheday.core.ui.ScrollableContainer
import com.jingom.seizetheday.core.ui.SimpleToolBar
import com.jingom.seizetheday.domain.model.Feeling
import com.jingom.seizetheday.presentation.ui.theme.SeizeTheDayTheme

@Composable
fun WritingThanksContentScreen(
	state: WritingThanksScreenState,
	onThanksContentChanged: (String) -> Unit = {},
	onSaveClick: () -> Unit = {},
	onWritingContentCancel: () -> Unit = {}
) {
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.background
	) {
		Column(modifier = Modifier.fillMaxSize()) {
			SimpleToolBar(
				hasNavigationButton = true,
				title = "감사일기 작성",
				onNavigateBackClick = onWritingContentCancel,
				modifier = Modifier
					.height(60.dp)
					.fillMaxWidth()
			)
			ScrollableContainer {
				Column(
					modifier = Modifier
						.wrapContentHeight()
						.fillMaxWidth(),
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Top
				) {
					if (state.feeling != null) {
						SelectedFeeling(feeling = state.feeling)
					}

					WriteThanksContentPromptMessage()

					Spacer(modifier = Modifier.height(20.dp))

					ContentForSelectedFeeling(
						content = state.content,
						onContentChanged = onThanksContentChanged,
						enabled = state.canEdit()
					)

					if (state.canSaveCurrentState()) {
						SaveButton(
							onClick = onSaveClick
						)
					}
				}
			}
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

@Preview
@Composable
fun SelectedFeelingPreview() {
	SelectedFeeling(feeling = Feeling.Happy)
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
fun ContentForSelectedFeeling(
	content: String,
	modifier: Modifier = Modifier,
	onContentChanged: (String) -> Unit = {},
	enabled: Boolean = true
) {
	TextField(
		modifier = modifier
			.padding(horizontal = 16.dp)
			.fillMaxWidth(),
		value = content,
		onValueChange = onContentChanged,
		enabled = enabled
	)
}

@Composable
fun SaveButton(onClick: () -> Unit = {}) {
	Button(onClick = onClick) {
		Text(text = "저장")
	}
}

@Preview
@Composable
fun SaveButtonPreview() {
	SaveButton()
}

@Preview
@Composable
fun WritingThanksContentScreenPreview() {
	SeizeTheDayTheme {
		WritingThanksContentScreen(
			state = WritingThanksScreenState(
				feeling = Feeling.Thanks,
				content = "오늘도 감사합니다."
			)
		)
	}
}