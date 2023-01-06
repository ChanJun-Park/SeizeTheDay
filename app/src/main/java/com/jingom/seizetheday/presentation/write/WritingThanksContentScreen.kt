package com.jingom.seizetheday.presentation.write

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jingom.seizetheday.core.ui.ScrollableContainer
import com.jingom.seizetheday.domain.Feeling

@Composable
fun WritingThanksContentScreen(
	state: WritingThanksScreenState,
	onThanksContentChanged: (String) -> Unit
) {
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
			ContentForSelectedFeeling(
				content = state.content,
				onContentChanged = onThanksContentChanged
			)
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
	onContentChanged: (String) -> Unit
) {
	TextField(value = content, onValueChange = onContentChanged)
}
