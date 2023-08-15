package com.jingom.seizetheday.presentation.page

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.jingom.seizetheday.domain.model.*
import com.jingom.seizetheday.presentation.write.SelectedFeeling
import java.time.LocalDate
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PageThanksScreen(viewModel: PageThanksViewModel = hiltViewModel()) {
	val pagingState = viewModel.thanksRecordWithImagesPagingData.collectAsLazyPagingItems()
	val startIndex by viewModel.startIndex.collectAsStateWithLifecycle()

	val pagerState = rememberPagerState(
		initialPage = startIndex,
		initialPageOffsetFraction = 0f
	) {
		pagingState.itemCount
	}

	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.surface.copy(alpha = 0.1f)
	) {
		HorizontalPager(
			modifier = Modifier,
			state = pagerState,
			pageSpacing = 0.dp,
			userScrollEnabled = true,
			reverseLayout = false,
			contentPadding = PaddingValues(horizontal = 30.dp),
			beyondBoundsPageCount = 0,
			key = null
		) { page ->
			pagingState[page]?.let {
				DayThanksPage(
					thanksRecordWithImages = it,
					modifier = Modifier.graphicsLayer {
						val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

						lerp(
							start = 0.85f,
							stop = 1f,
							fraction = 1f - pageOffset.coerceIn(0f, 1f)
						).also { scale ->
							scaleX = scale
							scaleY = scale
						}

						alpha = lerp(
							start = 0.5f,
							stop = 1f,
							fraction = 1f - pageOffset.coerceIn(0f, 1f)
						)
					}
				)
			}
		}
	}
}

@Composable
private fun DayThanksPage(
	thanksRecordWithImages: ThanksRecordWithImages,
	modifier: Modifier = Modifier
) {
	Surface(
		elevation = 1.dp,
		modifier = modifier
			.clip(RoundedCornerShape(30.dp))
			.fillMaxWidth()
			.height(700.dp)
	) {
		Column(
			modifier = Modifier
				.padding(20.dp)
				.verticalScroll(rememberScrollState())
				.fillMaxSize()
		) {
			Text(
				text = thanksRecordWithImages.thanksRecord.date.toString(),
				style = MaterialTheme.typography.h6
			)

			CircleDashHorizontalDivider()

			SelectedFeeling(thanksRecordWithImages.thanksRecord.feeling)

			Spacer(modifier = Modifier.height(20.dp))

			AttachedImageLayout(
				images = thanksRecordWithImages.attachedImageList,
				modifier = Modifier.fillMaxWidth()
			)

			Text(
				text = thanksRecordWithImages.thanksRecord.thanksContent,
				style = MaterialTheme.typography.body1
			)
		}

	}
}

@Composable
private fun AttachedImageLayout(
	images: AttachedImageList,
	modifier: Modifier = Modifier
) {
	LazyRow(
		horizontalArrangement = Arrangement.spacedBy(5.dp),
		verticalAlignment = Alignment.CenterVertically,
		modifier = modifier
	) {
		items(images.getImageList()) {
			AttachedImageUI(
				imageModel = it.imageUri,
				modifier = Modifier
					.clip(RoundedCornerShape(5.dp))
					.clickable { }
					.size(150.dp)
			)
		}
	}
}

@Composable
private fun AttachedImageUI(
	imageModel: String,
	modifier: Modifier = Modifier
) {
	AsyncImage(
		model = imageModel,
		contentDescription = null,
		contentScale = ContentScale.Crop,
		modifier = modifier
	)
}

@Preview
@Composable
fun DayThanksPagePreview() {

	val dummyThanksRecord = ThanksRecord(
		id = 1,
		feeling = Feeling.Thanks,
		thanksContent = LoremIpsum(200).values.joinToString(separator = " "),
		date = LocalDate.of(2023, 1, 28)
	)
	val dummyThanksRecordWithImages = ThanksRecordWithImages(
		thanksRecord = dummyThanksRecord,
		attachedImageList = AttachedImageList.emptyAttachedImageList()
	)

	DayThanksPage(dummyThanksRecordWithImages)
}

@Composable
private fun CircleDashHorizontalDivider(
	modifier: Modifier = Modifier
) {
	Canvas(
		modifier = modifier
			.fillMaxWidth()
			.height(20.dp)
	) {
		drawLine(
			color = Color.Black,
			start = Offset(0f, size.height / 2),
			end = Offset(size.width, size.height / 2),
			strokeWidth = 2.dp.toPx(),
			pathEffect = PathEffect.dashPathEffect(
				intervals = floatArrayOf(2.dp.toPx(), 2.dp.toPx()),
				phase = -1.dp.toPx()
			)
		)
	}
}

@Preview(showBackground = true)
@Composable
fun CircleDashHorizontalDividerPreview() {
	CircleDashHorizontalDivider(
		modifier = Modifier
			.fillMaxWidth()
			.height(20.dp)
	)
}
