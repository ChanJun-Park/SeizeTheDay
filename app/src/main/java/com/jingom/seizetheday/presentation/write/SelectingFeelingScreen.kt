package com.jingom.seizetheday.presentation.write

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jingom.seizetheday.core.ui.ScrollableContainer
import com.jingom.seizetheday.domain.model.Feeling

@Composable
fun SelectFeelingScreen(
	state: WritingThanksScreenState,
	onFeelingSelected: (Feeling) -> Unit = {}
) {
	ScrollableContainer {
		Column(
			modifier = Modifier
				.wrapContentHeight()
				.fillMaxWidth(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Top
		) {
			Spacer(modifier = Modifier.height(30.dp))

			SelectFeelingPromptMessage()

			Spacer(modifier = Modifier.height(30.dp))

			FeelingSelectSection(
				currentlySelectedFeeling = state.feeling,
				onFeelingSelected = onFeelingSelected
			)
		}
	}
}

@Composable
fun SelectFeelingPromptMessage() {
	Text(
		modifier = Modifier.width(200.dp),
		text = "지금 기분이 어떠신가요?",
		style = MaterialTheme.typography.h4
	)
}

@Preview
@Composable
fun SelectFeelingPromptMessagePreview() {
	SelectFeelingPromptMessage()
}

@Composable
fun FeelingItem(
	feeling: Feeling,
	selected: Boolean = false,
	onItemClick: (Feeling) -> Unit = {}
) {
	var modifier = Modifier
		.clickable { onItemClick(feeling) }
		.padding(10.dp)

	if (selected) {
		modifier = modifier.border(
			width = 1.dp,
			color = MaterialTheme.colors.primary,
			shape = RoundedCornerShape(10.dp)
		)
	}

	Box(
		modifier = modifier
	) {
		Text(
			modifier = Modifier
				.padding(5.dp),
			text = feeling.name,
			style = MaterialTheme.typography.body1
		)
	}
}

@Preview
@Composable
fun FeelingItemPreview() {
	FeelingItem(
		feeling = Feeling.Thanks,
		selected = true
	)
}

@Composable
fun FeelingSelectSection(
	currentlySelectedFeeling: Feeling?,
	onFeelingSelected: (Feeling) -> Unit = {}
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
			FeelingItem(feeling = Feeling.Happy, onItemClick = onFeelingSelected, selected = currentlySelectedFeeling == Feeling.Happy)
			FeelingItem(feeling = Feeling.Joy, onItemClick = onFeelingSelected, selected = currentlySelectedFeeling == Feeling.Joy)
			FeelingItem(feeling = Feeling.Hope, onItemClick = onFeelingSelected, selected = currentlySelectedFeeling == Feeling.Hope)
		}
		Row(
			modifier = Modifier.wrapContentSize()
		) {
			FeelingItem(feeling = Feeling.Thanks, onItemClick = onFeelingSelected, selected = currentlySelectedFeeling == Feeling.Thanks)
			FeelingItem(feeling = Feeling.Pride, onItemClick = onFeelingSelected, selected = currentlySelectedFeeling == Feeling.Pride)
			FeelingItem(feeling = Feeling.Serenity, onItemClick = onFeelingSelected, selected = currentlySelectedFeeling == Feeling.Serenity)
			FeelingItem(feeling = Feeling.Awe, onItemClick = onFeelingSelected, selected = currentlySelectedFeeling == Feeling.Awe)
		}
	}
}

@Preview
@Composable
fun SelectFeelingScreenPreview() {
	val state = WritingThanksScreenState(feeling = Feeling.Thanks)
	SelectFeelingScreen(state)
}
