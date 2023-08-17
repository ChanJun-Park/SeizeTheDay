package com.jingom.seizetheday.presentation.write

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.jingom.seizetheday.R
import com.jingom.seizetheday.core.permission.PermissionRequiredAction
import com.jingom.seizetheday.core.ui.LocalImagePickerActivity
import com.jingom.seizetheday.core.ui.SimpleToolBar
import com.jingom.seizetheday.core.ui.VerticalScrollableContainer
import com.jingom.seizetheday.domain.model.AttachedImage
import com.jingom.seizetheday.domain.model.Feeling
import com.jingom.seizetheday.presentation.getResourceString
import com.jingom.seizetheday.presentation.ui.theme.SeizeTheDayTheme
import kotlinx.coroutines.launch

private const val TEMP_MAX_IMAGE_PICK_COUNT = 10

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WritingThanksContentScreen(
	viewModel: WritingThanksViewModel = hiltViewModel(),
	onWritingContentCancel: () -> Unit = {}
) {
	val scaffoldState = rememberScaffoldState()
	val coroutineScope = rememberCoroutineScope()
	val imagePickerLauncher = rememberLauncherForActivityResult(
		contract = LocalImagePickerActivity.PickLocalImages(),
		onResult = viewModel::attachSelectedImages
	)

	val imagePermissionMessage = stringResource(R.string.media_image_permission_rationale)
	val mediaPermissionState = rememberMultiplePermissionsState(
		permissions = PermissionRequiredAction.ReadMediaImage.getRequiredPermissions(),
		onPermissionsResult = { resultMap ->
			if (resultMap.allPermissionGranted()) {
				imagePickerLauncher.launch(LocalImagePickerActivity.ImagePickOptions(TEMP_MAX_IMAGE_PICK_COUNT))
			} else {
				coroutineScope.launch {
					scaffoldState.snackbarHostState.showSnackbar(imagePermissionMessage)
				}
			}
		}
	)

	val state by viewModel.writingThanksScreenState.collectAsState()
	val attachedImages by viewModel.attachedImageListState.collectAsState()
	var needToShowPermissionAlert by remember { mutableStateOf(false) }

	WritingThanksContentScreen(
		state = state,
		scaffoldState = scaffoldState,
		attachedImages = attachedImages,
		onThanksContentChanged = viewModel::changeThanksContent,
		onSaveClick = viewModel::save,
		onWritingContentCancel = onWritingContentCancel,
		onImageAttachClick = {
			if (mediaPermissionState.allPermissionsGranted) {
				imagePickerLauncher.launch(LocalImagePickerActivity.ImagePickOptions(TEMP_MAX_IMAGE_PICK_COUNT))
			} else {
				needToShowPermissionAlert = true
			}
		}
	)

	if (needToShowPermissionAlert) {
		ReadMediaImagePermissionDialog(
			mediaPermissionState = mediaPermissionState,
			onDismissRequest = { needToShowPermissionAlert = false }
		)
	}
}

fun Map<String, Boolean>.allPermissionGranted() = this.values.all { it }

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ReadMediaImagePermissionDialog(
	mediaPermissionState: MultiplePermissionsState,
	onDismissRequest: () -> Unit = {}
) {
	AlertDialog(
		text = {
			val textToShow = if (mediaPermissionState.shouldShowRationale) {
				stringResource(R.string.media_image_permission_rationale)
			} else {
				stringResource(R.string.media_image_permission_description)
			}

			Text(textToShow)
		},
		confirmButton = {
			Button(
				onClick = {
					mediaPermissionState.launchMultiplePermissionRequest()
					onDismissRequest()
				}
			) {
				Text(stringResource(R.string.request_permission))
			}
		},
		onDismissRequest = onDismissRequest
	)
}

@Composable
fun WritingThanksContentScreen(
	state: WritingThanksScreenState,
	scaffoldState: ScaffoldState = rememberScaffoldState(),
	attachedImages: List<AttachedImage> = emptyList(),
	onThanksContentChanged: (String) -> Unit = {},
	onSaveClick: () -> Unit = {},
	onImageAttachClick: () -> Unit = {},
	onWritingContentCancel: () -> Unit = {}
) {
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.background
	) {
		Scaffold(
			topBar = {
				SimpleToolBar(
					hasNavigationButton = true,
					title = "감사일기 작성",
					onNavigateBackClick = onWritingContentCancel,
					modifier = Modifier
						.height(60.dp)
						.fillMaxWidth()
				)
			},
			scaffoldState = scaffoldState,
			modifier = Modifier.fillMaxSize()
		) {
			VerticalScrollableContainer(
				horizontalAlignment = Alignment.CenterHorizontally,
				modifier = Modifier
					.padding(it)
					.fillMaxSize()
			) {
				WriteThanksContentPromptMessage()

				if (attachedImages.isNotEmpty()) {
					AttachedImageLayer(
						imageList = attachedImages,
						modifier = Modifier
							.padding(horizontal = 20.dp)
							.fillMaxWidth()
							.height(300.dp)
					)
				}

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AttachedImageLayer(
	modifier: Modifier = Modifier,
	imageList: List<AttachedImage> = emptyList()
) {
	HorizontalPager(
		modifier = modifier.clip(RoundedCornerShape(5.dp)),
		state = rememberPagerState { imageList.size },
		pageSpacing = 0.dp,
		userScrollEnabled = true,
		reverseLayout = false,
		contentPadding = PaddingValues(0.dp),
		beyondBoundsPageCount = 0,
		key = null,
	) { pageIndex ->
		val attachedImage = imageList.getImageForIndex(pageIndex) ?: return@HorizontalPager

		AttachedImageComponent(
			attachedImage = attachedImage,
			modifier = Modifier
				.fillMaxSize()
				.clip(RoundedCornerShape(5.dp))
		)
	}
}

@Composable
private fun AttachedImageComponent(
	attachedImage: AttachedImage,
	modifier: Modifier
) {
	AsyncImage(
		model = attachedImage.imageUri,
		contentDescription = null,
		contentScale = ContentScale.Crop,
		modifier = modifier
	)
}

private fun List<AttachedImage>.getImageForIndex(index: Int): AttachedImage? {
	return if (index in indices) {
		get(index)
	} else {
		null
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
fun ImageAttachButton(
	modifier: Modifier = Modifier,
	onClick: () -> Unit = {}
) {
	Button(
		modifier = modifier,
		onClick = onClick
	) {
		Text(text = "이미지 추가")
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