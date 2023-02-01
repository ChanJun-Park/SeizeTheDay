package com.jingom.seizetheday.presentation.page

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.jingom.seizetheday.domain.model.Feeling
import com.jingom.seizetheday.domain.model.ThanksRecord
import com.jingom.seizetheday.presentation.write.SelectedFeeling
import java.time.LocalDate
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PageThanksScreen(
	startThanksId: Int? = null,
	viewModel: PageThanksViewModel = hiltViewModel()
) {
	val pagingState = viewModel.thanksRecordsPagingData.collectAsLazyPagingItems()
	val pagerState = rememberPagerState()
	var lastViewingThanksId by remember {
		mutableStateOf(startThanksId)
	}
	
	LaunchedEffect(pagerState) {
		snapshotFlow { pagerState.currentPage }.collect { page ->
			if (page < 0 || page >= pagingState.itemCount) {
				return@collect
			}

			lastViewingThanksId = pagingState.peek(page)?.id ?: return@collect
		}
	}

	LaunchedEffect(key1 = pagingState.itemSnapshotList) {
		if (pagingState.itemCount == 0) {
			return@LaunchedEffect
		}

		var scrollTargetPage = pagingState.itemSnapshotList.items.indexOfFirst { it.id == lastViewingThanksId }
		if (scrollTargetPage == -1) {
			scrollTargetPage = 0
		}

		pagerState.scrollToPage(scrollTargetPage)
	}

	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.surface.copy(alpha = 0.1f)
	) {
		HorizontalPager(
			count = pagingState.itemCount,
			contentPadding = PaddingValues(horizontal = 30.dp),
			state = pagerState
		) { index ->
			pagingState[index]?.let {
				DayThanksPage(
					thanksRecord = it,
					modifier = Modifier.graphicsLayer {
						val pageOffset = calculateCurrentOffsetForPage(index).absoluteValue

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
	thanksRecord: ThanksRecord,
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
				text = thanksRecord.date.toString(),
				style = MaterialTheme.typography.h6
			)

			CircleDashHorizontalDivider()

			SelectedFeeling(thanksRecord.feeling)

			Spacer(modifier = Modifier.height(20.dp))

			Text(
				text = thanksRecord.thanksContent,
				style = MaterialTheme.typography.body1
			)
		}

	}
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

	DayThanksPage(dummyThanksRecord)
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
