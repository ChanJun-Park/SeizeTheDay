package com.jingom.seizetheday.core.ui

import android.net.Uri
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jingom.seizetheday.R
import com.jingom.seizetheday.core.isInspectionMode
import com.jingom.seizetheday.data.media.model.MediaImage
import com.jingom.seizetheday.data.media.model.MediaImageAlbum

sealed class LocalImagePickerUiState {
	data class ImageSelectionState(
		val imageList: List<MediaImage> = emptyList(),
		val selectedImageList: List<MediaImage> = emptyList(),
	): LocalImagePickerUiState()

	data class AlbumSelectionState(
		val albumList: List<MediaImageAlbum> = emptyList()
	): LocalImagePickerUiState()
}

@Composable
fun LocalImagePickerScreen(
	localImagePickerUiState: LocalImagePickerUiState,
	modifier: Modifier = Modifier,
	onImageClick: (MediaImage) -> Unit = {},
	onAlbumClick: (MediaImageAlbum) -> Unit = {}
) {
	Surface(modifier = modifier.fillMaxSize()) {
		Box(modifier = Modifier.fillMaxSize()) {

			ImagePickerToolbar(
				modifier = Modifier
					.fillMaxWidth()
					.height(60.dp)
					.background(color = MaterialTheme.colors.surface.copy(alpha = 0.1f))
			)

			when (localImagePickerUiState) {
				is LocalImagePickerUiState.ImageSelectionState -> {
					ImageList(
						imageList = localImagePickerUiState.imageList,
						onImageClick = onImageClick,
						modifier = Modifier.fillMaxSize()
					)
				}
				is LocalImagePickerUiState.AlbumSelectionState -> {
					AlbumList(
						albumList = localImagePickerUiState.albumList,
						onAlbumClick = onAlbumClick,
						modifier = Modifier.fillMaxSize()
					)
				}
			}
		}
	}
}

@Preview
@Composable
private fun LocalImagePickerScreenPreview() {
	val dummyImageList = listOf(
		MediaImage(1, 1, Uri.EMPTY, "image/png", 0, 0),
		MediaImage(2, 2, Uri.EMPTY, "image/png", 0, 0),
		MediaImage(3, 3, Uri.EMPTY, "image/png", 0, 0),
		MediaImage(4, 4, Uri.EMPTY, "image/png", 0, 0),
		MediaImage(5, 5, Uri.EMPTY, "image/png", 0, 0),
		MediaImage(6, 6, Uri.EMPTY, "image/png", 0, 0),
		MediaImage(7, 7, Uri.EMPTY, "image/png", 0, 0),
		MediaImage(8, 8, Uri.EMPTY, "image/png", 0, 0),
	)
	val localImagePickerUiState = LocalImagePickerUiState.ImageSelectionState(
		imageList = dummyImageList
	)
	LocalImagePickerScreen(
		localImagePickerUiState = localImagePickerUiState,
		modifier = Modifier.fillMaxSize()
	)
}

@Composable
private fun ImageList(
	imageList: List<MediaImage>,
	modifier: Modifier = Modifier,
	onImageClick: (MediaImage) -> Unit = {}
) {
	LazyVerticalGrid(
		columns = GridCells.Adaptive(minSize = 100.dp),
		verticalArrangement = Arrangement.spacedBy(5.dp),
		horizontalArrangement = Arrangement.spacedBy(5.dp),
		contentPadding = PaddingValues(top = 65.dp),
		modifier = modifier.fillMaxSize()
	) {
		items(
			items = imageList,
			key = MediaImage::id
		) {
			SingleLocalImage(
				mediaImage = it,
				onClick = onImageClick,
				modifier = Modifier.aspectRatio(1f)
			)
		}
	}
}

@Composable
private fun AlbumList(
	albumList: List<MediaImageAlbum>,
	modifier: Modifier = Modifier,
	onAlbumClick: (MediaImageAlbum) -> Unit = {}
) {
	LazyColumn(modifier = modifier.fillMaxSize()) {
		items(
			items = albumList,
			key = MediaImageAlbum::albumId
		) {
			SingleLocalImageAlbum(
				imageAlbum = it,
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

@Preview(showBackground = true)
@Composable
private fun AlbumListPreview() {
	val dummyImageAlbumList = listOf(
		MediaImageAlbum(
			albumId = 1,
			albumName = "Camera",
			thumbnailImage = MediaImage(1, 1, Uri.EMPTY, "image/png", 0, 0),
			imageCount = 10
		),
		MediaImageAlbum(
			albumId = 2,
			albumName = "Kakao",
			thumbnailImage = MediaImage(2, 2, Uri.EMPTY, "image/png", 0, 0),
			imageCount = 10
		),
		MediaImageAlbum(
			albumId = 3,
			albumName = "Naver",
			thumbnailImage = MediaImage(3, 3, Uri.EMPTY, "image/png", 0, 0),
			imageCount = 10
		),
		MediaImageAlbum(
			albumId = 4,
			albumName = "Calendar",
			thumbnailImage = MediaImage(4, 4, Uri.EMPTY, "image/png", 0, 0),
			imageCount = 10
		),
		MediaImageAlbum(
			albumId = 5,
			albumName = "DDD",
			thumbnailImage = MediaImage(5, 5, Uri.EMPTY, "image/png", 0, 0),
			imageCount = 10
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
	onClick: (MediaImage) -> Unit = {}
) {
	Card(modifier = modifier.clickable { onClick(mediaImage) }) {
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

		SingleLocalImage(
			mediaImage = imageAlbum.thumbnailImage,
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
				text = imageAlbum.albumName,
				style = MaterialTheme.typography.body1
			)
			Text(
				text = imageAlbum.imageCount.toString(),
				style = MaterialTheme.typography.caption
			)
		}
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
	modifier: Modifier = Modifier,
	title: String = "카메라 롤",
	onBackButtonClick: () -> Unit = {},
	onAlbumTitleClick: () -> Unit = {},
	isAlbumListExpanded: Boolean = false
) {
	Box(modifier = modifier) {
		NavigateBackButton(
			onClick = onBackButtonClick,
			modifier = Modifier.align(Alignment.CenterStart)
		)

		Row(
			verticalAlignment = Alignment.CenterVertically,
			modifier = Modifier
				.wrapContentSize()
				.align(Alignment.Center)
				.clickable { onAlbumTitleClick() }
		) {
			Text(
				text = title,
				style = MaterialTheme.typography.h6
			)

			val expendMoreButtonDegree = animateIntAsState(
				targetValue = if (isAlbumListExpanded) 180 else 0,
				animationSpec = tween(easing = LinearEasing)
			)

			Icon(
				painter = painterResource(id = R.drawable.ic_expand_more),
				contentDescription = "다른 앨범 보기",
				modifier = Modifier
					.rotate(expendMoreButtonDegree.value.toFloat())
					.padding(start = 10.dp)
			)
		}
	}
}

@Preview
@Composable
private fun ImagePickerToolbarPreview() {
	ImagePickerToolbar(
		modifier = Modifier
			.fillMaxWidth()
			.height(60.dp)
	)
}