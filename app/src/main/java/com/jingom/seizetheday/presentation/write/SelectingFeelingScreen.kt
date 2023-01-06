package com.jingom.seizetheday.presentation.write

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jingom.seizetheday.core.ui.ScrollableContainer
import com.jingom.seizetheday.domain.Feeling

@Composable
fun SelectFeelingScreen(
	state: WritingThanksScreenState,
	onFeelingSelected: (Feeling) -> Unit
) {
	ScrollableContainer {
		Column(
			modifier = Modifier
				.wrapContentHeight()
				.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Top
		) {
			SelectFeelingPromptMessage()
			FeelingSelectSection(
				currentlySelectedFeeling = state.feeling,
				onFeelingSelected = onFeelingSelected
			)
		}
	}
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

@Composable
fun FeelingSelectSection(
	currentlySelectedFeeling: Feeling?,
	onFeelingSelected: (Feeling) -> Unit
) {
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

