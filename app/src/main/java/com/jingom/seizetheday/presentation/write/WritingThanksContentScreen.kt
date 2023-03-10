package com.jingom.seizetheday.presentation.write

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jingom.seizetheday.core.ui.LocalImagePickerActivity
import com.jingom.seizetheday.core.ui.SimpleToolBar
import com.jingom.seizetheday.core.ui.VerticalScrollableContainer
import com.jingom.seizetheday.domain.model.Feeling
import com.jingom.seizetheday.presentation.getResourceString
import com.jingom.seizetheday.presentation.ui.theme.SeizeTheDayTheme

@Composable
fun WritingThanksContentScreen(
	viewModel: WritingThanksViewModel = hiltViewModel(),
	onWritingContentCancel: () -> Unit = {}
) {
	val context = LocalContext.current
	val imagePickerLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.StartActivityForResult()
	) { result ->

	}
	val state by viewModel.writingThanksScreenState.collectAsState()
	WritingThanksContentScreen(
		state = state,
		onThanksContentChanged = viewModel::changeThanksContent,
		onSaveClick = viewModel::save,
		onWritingContentCancel = onWritingContentCancel,
		onImageAttachClick = {
			val intent = LocalImagePickerActivity.getIntent(context)
			imagePickerLauncher.launch(intent)
		}
	)
}

@Composable
fun WritingThanksContentScreen(
	state: WritingThanksScreenState,
	onThanksContentChanged: (String) -> Unit = {},
	onSaveClick: () -> Unit = {},
	onImageAttachClick: () -> Unit = {},
	onWritingContentCancel: () -> Unit = {}
) {
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.background
	) {
		Column(modifier = Modifier.fillMaxSize()) {
			SimpleToolBar(
				hasNavigationButton = true,
				title = "???????????? ??????",
				onNavigateBackClick = onWritingContentCancel,
				modifier = Modifier
					.height(60.dp)
					.fillMaxWidth()
			)
			VerticalScrollableContainer(
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.SpaceBetween,
				modifier = Modifier.fillMaxSize()
			) {
				ContentLayer(
					state = state,
					onThanksContentChanged = onThanksContentChanged,
					modifier = Modifier,
				)

				ImageAttachButton(
					onClick = onImageAttachClick
				)

				if (state.canSaveCurrentState()) {
					SaveButton(
						onClick = onSaveClick,
						modifier = Modifier.padding(bottom = 20.dp)
					)
				}
			}
		}
	}
}

@Composable
private fun ContentLayer(
	state: WritingThanksScreenState,
	modifier: Modifier = Modifier,
	onThanksContentChanged: (String) -> Unit = {}
) {
	Column(
		modifier = modifier
			.wrapContentHeight()
			.fillMaxWidth(),
		horizontalAlignment = Alignment.CenterHorizontally
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
	}
}

@Composable
fun SelectedFeeling(feeling: Feeling) {
	Text(
		text = feeling.getResourceString(),
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
	Text(text = "????????? ????????? ???????????????")
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
fun ImageAttachButton(
	modifier: Modifier = Modifier,
	onClick: () -> Unit = {}
) {
	Button(
		modifier = modifier,
		onClick = onClick
	) {
		Text(text = "????????? ??????")
	}
}

@Composable
fun SaveButton(
	modifier: Modifier = Modifier,
	onClick: () -> Unit = {}
) {
	Button(
		modifier = modifier,
		onClick = onClick
	) {
		Text(text = "??????")
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
				content = "????????? ???????????????."
			)
		)
	}
}