package com.jingom.seizetheday.core.ui

import android.net.Uri
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jingom.seizetheday.R
import com.jingom.seizetheday.core.isInspectionMode
import com.jingom.seizetheday.data.media.model.MediaImage
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale

@Composable
fun LocalImagePickerScreen(
	imageList: List<MediaImage>,
	modifier: Modifier = Modifier
) {
	Surface(modifier = modifier.fillMaxSize()) {
		Box(modifier = Modifier.fillMaxSize()) {

			ImagePickerToolbar(
				modifier = Modifier
					.fillMaxWidth()
					.height(60.dp)
					.background(color = MaterialTheme.colors.surface.copy(alpha = 0.1f))
			)

			LazyVerticalGrid(
				columns = GridCells.Adaptive(minSize = 100.dp),
				verticalArrangement = Arrangement.spacedBy(5.dp),
				horizontalArrangement = Arrangement.spacedBy(5.dp),
				contentPadding = PaddingValues(top = 65.dp),
				modifier = Modifier.fillMaxSize()
			) {
				items(
					items = imageList,
					key = MediaImage::id
				) {
					SingleLocalImage(
						mediaImage = it,
						modifier = Modifier.aspectRatio(1f)
					)
				}
			}
		}
	}
}

@Preview
@Composable
fun LocalImagePickerScreenPreview() {
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
	LocalImagePickerScreen(
		imageList = dummyImageList,
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
fun ImagePickerToolbar(
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
fun ImagePickerToolbarPreview() {
	ImagePickerToolbar(
		modifier = Modifier
			.fillMaxWidth()
			.height(60.dp)
	)
}