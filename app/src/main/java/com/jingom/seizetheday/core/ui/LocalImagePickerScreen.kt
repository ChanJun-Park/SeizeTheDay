package com.jingom.seizetheday.core.ui

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.jingom.seizetheday.R
import com.jingom.seizetheday.core.BackPressHandler
import com.jingom.seizetheday.core.UIText
import com.jingom.seizetheday.core.isInspectionMode
import com.jingom.seizetheday.domain.LocalMediaImageLoader
import com.jingom.seizetheday.domain.model.media.MediaImage
import com.jingom.seizetheday.domain.model.media.MediaImageAlbum

data class ImageListUiState(
	val imageList: List<MediaImageUiState> = emptyList()
)

data class AlbumListUiState(
	val albumList: List<MediaImageAlbumUiState> = emptyList()
)

data class MediaImageUiState(
	val mediaImage: MediaImage,
	val isSelected: Boolean,
	val selectionNumber: Int
)

data class MediaImageAlbumUiState(
	val mediaImageAlbum: MediaImageAlbum,
	val isSelected: Boolean
)

@Composable
fun LocalImagePickerScreen(
	viewModel: LocalImagePickerViewModel = hiltViewModel(),
	onBackButtonClick: () -> Unit = {},
	onAttachButtonClick: () -> Unit = {}
) {
	val imageListUiState by viewModel.imageListUiState.collectAsStateWithLifecycle()
	val albumListUiState by viewModel.albumListUiState.collectAsStateWithLifecycle()
	val selectedImageAlbum by viewModel.selectedImageAlbum.collectAsStateWithLifecycle()
	val errorMessages by viewModel.errorMessages.collectAsStateWithLifecycle()
	val context = LocalContext.current

	var isAlbumListVisible by remember {
		mutableStateOf(false)
	}

	LocalImagePickerScreen(
		selectedImageAlbum = selectedImageAlbum,
		imageListUiState = imageListUiState,
		albumListUiState = albumListUiState,
		isAlbumListVisible = isAlbumListVisible,
		onImageClick = viewModel::toggleImageSelectionState,
		onAlbumClick = {
			viewModel.changeSelectedAlbum(it)
			isAlbumListVisible = !isAlbumListVisible
		},
		onBackButtonClick = onBackButtonClick,
		onAlbumTitleClick = { isAlbumListVisible = !isAlbumListVisible },
		onAttachButtonClick = onAttachButtonClick
	)

	BackPressHandler(
		onBackPressed = {
			if (isAlbumListVisible) {
				isAlbumListVisible = false
				return@BackPressHandler
			}

			onBackButtonClick()
		}
	)

	if (errorMessages.isNotEmpty()) {
		LaunchedEffect(key1 = errorMessages) {
			showErrorMessage(errorMessages, context, viewModel)
		}
	}
}

private fun showErrorMessage(
	errorMessages: List<UIText>,
	context: Context,
	viewModel: LocalImagePickerViewModel
) {
	if (errorMessages.isEmpty()) {
		return
	}

	val errorMessage = errorMessages[0]
	Toast.makeText(context, errorMessage.asString(context), Toast.LENGTH_SHORT).show()
	viewModel.shownErrorMessage(errorMessage)
}

@Composable
fun LocalImagePickerScreen(
	selectedImageAlbum: MediaImageAlbum,
	imageListUiState: ImageListUiState,
	albumListUiState: AlbumListUiState,
	modifier: Modifier = Modifier,
	isAlbumListVisible: Boolean = false,
	onImageClick: (MediaImage) -> Unit = {},
	onAlbumClick: (MediaImageAlbum) -> Unit = {},
	onBackButtonClick: () -> Unit = {},
	onAlbumTitleClick: () -> Unit = {},
	onAttachButtonClick: () -> Unit = {}
) {
	Surface(modifier = modifier.fillMaxSize()) {
		Box(modifier = Modifier.fillMaxSize()) {

			ImagePickerToolbar(
				title = selectedImageAlbum.getTitleString(),
				onBackButtonClick = onBackButtonClick,
				onAlbumTitleClick = onAlbumTitleClick,
				onAttachButtonClick = onAttachButtonClick,
				isAlbumListExpanded = isAlbumListVisible,
				modifier = Modifier
					.fillMaxWidth()
					.height(60.dp)
					.background(color = MaterialTheme.colors.surface.copy(alpha = 0.1f))
					.zIndex(3f)
			)

			ImageList(
				imageList = imageListUiState.imageList,
				onImageClick = onImageClick,
				modifier = Modifier
					.fillMaxSize()
					.zIndex(1f)
			)

			if (isAlbumListVisible) {
				AlbumList(
					albumList = albumListUiState.albumList,
					onAlbumClick = onAlbumClick,
					modifier = Modifier
						.fillMaxSize()
						.zIndex(2f)
				)
			}
		}
	}
}

@Preview
@Composable
private fun LocalImagePickerScreenPreview() {
	val dummyImageList = listOf(
		MediaImageUiState(MediaImage(1, 1, Uri.EMPTY, "image/png", 0, 0), true, 1),
		MediaImageUiState(MediaImage(2, 2, Uri.EMPTY, "image/png", 0, 0), true, 2),
		MediaImageUiState(MediaImage(3, 3, Uri.EMPTY, "image/png", 0, 0), false, 3),
		MediaImageUiState(MediaImage(4, 4, Uri.EMPTY, "image/png", 0, 0), true, 4),
		MediaImageUiState(MediaImage(5, 5, Uri.EMPTY, "image/png", 0, 0), false, 5),
		MediaImageUiState(MediaImage(6, 6, Uri.EMPTY, "image/png", 0, 0), false, 6),
		MediaImageUiState(MediaImage(7, 7, Uri.EMPTY, "image/png", 0, 0), false, 7),
		MediaImageUiState(MediaImage(8, 8, Uri.EMPTY, "image/png", 0, 0), false, 8),
	)
	val imageListUiState = ImageListUiState(
		imageList = dummyImageList
	)
	LocalImagePickerScreen(
		selectedImageAlbum = MediaImageAlbum(albumId = LocalMediaImageLoader.ALL_IMAGES_ALBUM_ID),
		imageListUiState = imageListUiState,
		albumListUiState = AlbumListUiState(emptyList()),
		modifier = Modifier.fillMaxSize()
	)
}

@Composable
private fun ImageList(
	imageList: List<MediaImageUiState>,
	modifier: Modifier = Modifier,
	onImageClick: (MediaImage) -> Unit = {}
) {
	Surface(modifier = modifier) {
		LazyVerticalGrid(
			columns = GridCells.Adaptive(minSize = 100.dp),
			verticalArrangement = Arrangement.spacedBy(5.dp),
			horizontalArrangement = Arrangement.spacedBy(5.dp),
			contentPadding = PaddingValues(top = 65.dp),
			modifier = Modifier.fillMaxSize()
		) {
			items(
				items = imageList,
				key = { it.mediaImage.id }
			) {
				SingleLocalImage(
					mediaImage = it.mediaImage,
					isSelected = it.isSelected,
					onClick = onImageClick,
					modifier = Modifier.aspectRatio(1f)
				)
			}
		}
	}
}

@Composable
private fun AlbumList(
	albumList: List<MediaImageAlbumUiState>,
	modifier: Modifier = Modifier,
	onAlbumClick: (MediaImageAlbum) -> Unit = {}
) {
	Surface(modifier = modifier) {
		LazyColumn(
			contentPadding = PaddingValues(top = 65.dp),
			modifier = Modifier.fillMaxSize()
		) {
			items(
				items = albumList,
				key = { it.mediaImageAlbum.albumId }
			) {
				SingleLocalImageAlbum(
					imageAlbum = it.mediaImageAlbum,
					onClick = onAlbumClick,
					modifier = Modifier
						.fillMaxWidth()
						.height(60.dp)
				)
				Spacer(
					modifier = Modifier
						.fillMaxWidth()
						.height(1.dp)
						.background(color = Color.Gray.copy(alpha = 0.1f))
				)
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
private fun AlbumListPreview() {
	val dummyImageAlbumList = listOf(
		MediaImageAlbumUiState(
			mediaImageAlbum = MediaImageAlbum(
				albumId = 1,
				albumName = "Camera",
				thumbnailImage = MediaImage(1, 1, Uri.EMPTY, "image/png", 0, 0),
				imageCount = 10
			),
			isSelected = false
		),
		MediaImageAlbumUiState(
			mediaImageAlbum = MediaImageAlbum(
				albumId = 2,
				albumName = "Kakao",
				thumbnailImage = MediaImage(2, 2, Uri.EMPTY, "image/png", 0, 0),
				imageCount = 10
			),
			isSelected = false
		),
		MediaImageAlbumUiState(
			mediaImageAlbum = MediaImageAlbum(
				albumId = 3,
				albumName = "Naver",
				thumbnailImage = MediaImage(3, 3, Uri.EMPTY, "image/png", 0, 0),
				imageCount = 10
			),
			isSelected = false
		),
		MediaImageAlbumUiState(
			mediaImageAlbum = MediaImageAlbum(
				albumId = 4,
				albumName = "Calendar",
				thumbnailImage = MediaImage(4, 4, Uri.EMPTY, "image/png", 0, 0),
				imageCount = 10
			),
			isSelected = false
		),
		MediaImageAlbumUiState(
			mediaImageAlbum = MediaImageAlbum(
				albumId = 5,
				albumName = "DDD",
				thumbnailImage = MediaImage(5, 5, Uri.EMPTY, "image/png", 0, 0),
				imageCount = 10
			),
			isSelected = false
		)
	)

	AlbumList(
		albumList = dummyImageAlbumList,
		modifier = Modifier.fillMaxSize()
	)
}

@Composable
private fun SingleLocalImage(
	mediaImage: MediaImage,
	modifier: Modifier = Modifier,
	isSelected: Boolean = false,
	onClick: (MediaImage) -> Unit = {}
) {
	Card(
		modifier = modifier
			.selectedBorder(isSelected)
			.clickable { onClick(mediaImage) }
	) {
		if (isInspectionMode) {
			Image(
				painter = painterResource(id = R.drawable.android),
				contentDescription = null,
				modifier = Modifier.fillMaxSize()
			)
			return@Card
		}

		AsyncImage(
			model = mediaImage.contentUri,
			contentDescription = null,
			contentScale = ContentScale.Crop,
			modifier = Modifier.fillMaxSize(),
		)
	}
}

private fun Modifier.selectedBorder(isSelected: Boolean) = composed {
	if (isSelected) {
		border(
			width = 2.dp,
			color = Color.Green,
			shape = MaterialTheme.shapes.medium
		)
	} else {
		this
	}
}

@Composable
private fun SingleLocalImageAlbum(
	imageAlbum: MediaImageAlbum,
	modifier: Modifier = Modifier,
	onClick: (MediaImageAlbum) -> Unit = {}
) {
	Row(
		modifier = modifier
			.padding(
				horizontal = 20.dp,
				vertical = 10.dp
			)
			.clickable { onClick(imageAlbum) }
	) {
		AlbumThumbnail(
			imageAlbum = imageAlbum,
			modifier = Modifier
				.fillMaxHeight()
				.aspectRatio(1f)
		)

		Spacer(modifier = Modifier.width(10.dp))

		Column(
			verticalArrangement = Arrangement.SpaceBetween,
			modifier = Modifier.fillMaxSize()
		) {
			Text(
				text = imageAlbum.getTitleString(),
				style = MaterialTheme.typography.body1
			)
			Text(
				text = imageAlbum.imageCount.toString(),
				style = MaterialTheme.typography.caption
			)
		}
	}
}

@Composable
private fun AlbumThumbnail(
	imageAlbum: MediaImageAlbum,
	modifier: Modifier = Modifier
) {
	val thumbnail = imageAlbum.thumbnailImage
	if (thumbnail != null) {
		SingleLocalImage(
			mediaImage = thumbnail,
			modifier = modifier
		)
	} else {
		Spacer(modifier = modifier)
	}
}

@Preview(showBackground = true)
@Composable
private fun SingleLocalImageAlbumPreview() {
	SingleLocalImageAlbum(
		imageAlbum = MediaImageAlbum(
			albumId = 1,
			albumName = "Camera",
			thumbnailImage = MediaImage(1, 1, Uri.EMPTY, "image/png", 0, 0),
			imageCount = 30
		),
		modifier = Modifier
			.fillMaxWidth()
			.height(60.dp)
	)
}

@Composable
private fun ImagePickerToolbar(
	title: String,
	modifier: Modifier = Modifier,
	onBackButtonClick: () -> Unit = {},
	onAlbumTitleClick: () -> Unit = {},
	onAttachButtonClick: () -> Unit = {},
	isAlbumListExpanded: Boolean = false
) {
	Surface(modifier = modifier) {
		ConstraintLayout(modifier = Modifier.fillMaxSize()) {
			val (backButton, titleText, arrowIcon, attachButton) = createRefs()

			NavigateBackButton(
				onClick = onBackButtonClick,
				modifier = Modifier
					.constrainAs(backButton) {
						start.linkTo(parent.start)
						top.linkTo(parent.top)
						bottom.linkTo(parent.bottom)
					}
			)


			Text(
				text = title,
				style = MaterialTheme.typography.h6,
				modifier = Modifier
					.clickable { onAlbumTitleClick() }
					.constrainAs(titleText) {
						start.linkTo(parent.start)
						end.linkTo(parent.end)
						top.linkTo(parent.top)
						bottom.linkTo(parent.bottom)
					}
			)

			val expendMoreButtonDegree = animateIntAsState(
				targetValue = if (isAlbumListExpanded) 180 else 0,
				animationSpec = tween(easing = LinearEasing)
			)

			Icon(
				painter = painterResource(id = R.drawable.ic_expand_more),
				contentDescription = "다른 앨범 보기",
				modifier = Modifier
					.padding(start = 10.dp)
					.rotate(expendMoreButtonDegree.value.toFloat())
					.clickable { onAlbumTitleClick() }
					.constrainAs(arrowIcon) {
						start.linkTo(titleText.end)
						top.linkTo(parent.top)
						bottom.linkTo(parent.bottom)
					}
			)

			AttachButton(
				modifier = Modifier
					.padding(end = 18.dp)
					.clickable { onAttachButtonClick() }
					.sizeIn(minWidth = 48.dp, minHeight = 48.dp)
					.constrainAs(attachButton) {
						end.linkTo(parent.end)
						top.linkTo(parent.top)
						bottom.linkTo(parent.bottom)
					}
			)
		}
	}
}

@Composable
private fun AttachButton(
	modifier: Modifier = Modifier
) {
	Box(modifier = modifier) {
		Text(
			text = stringResource(R.string.attach),
			style = MaterialTheme.typography.h6,
			textAlign = TextAlign.Center,
			modifier = Modifier.align(Alignment.Center)
		)
	}
}

@Preview
@Composable
private fun ImagePickerToolbarPreview() {
	ImagePickerToolbar(
		title = "모든 이미지",
		modifier = Modifier
			.fillMaxWidth()
			.height(60.dp)
	)
}

@Composable
private fun MediaImageAlbum.getTitleString(): String {
	val context = LocalContext.current

	return if (albumId == LocalMediaImageLoader.ALL_IMAGES_ALBUM_ID) {
		context.getString(R.string.every_image)
	} else {
		albumName
	}
}