package com.jingom.seizetheday.core.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Surface
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
import androidx.compose.material.Card
import androidx.compose.ui.layout.ContentScale

@Composable
fun LocalImagePickerScreen(
	imageList: List<MediaImage>,
	modifier: Modifier = Modifier
) {
	Surface(modifier = modifier) {
		LazyVerticalGrid(
			columns = GridCells.Adaptive(minSize = 100.dp),
			verticalArrangement = Arrangement.spacedBy(5.dp),
			horizontalArrangement = Arrangement.spacedBy(5.dp),
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