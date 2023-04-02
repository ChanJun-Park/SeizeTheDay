package com.jingom.seizetheday.presentation.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import com.jingom.seizetheday.R
import com.jingom.seizetheday.domain.model.AttachedImageList
import com.jingom.seizetheday.domain.model.Feeling
import com.jingom.seizetheday.domain.model.ThanksRecord
import com.jingom.seizetheday.domain.model.ThanksRecordWithImages
import com.jingom.seizetheday.presentation.getResourceString
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed interface ListThanksRecordUiState {
	data class ThanksRecordItemWithImages(val thanksRecordWithImages: ThanksRecordWithImages) : ListThanksRecordUiState
	data class DateHeaderItem(val date: LocalDate) : ListThanksRecordUiState
}

// stateful
@Composable
fun ListThanksScreen(
	viewModel: ListThanksViewModel = hiltViewModel(),
	onNewThanksClick: () -> Unit = {},
	onThanksClick: (ThanksRecordWithImages) -> Unit = {}
) {
	val listThanksRecordUiModels = viewModel.thanksRecordsPagingData.collectAsLazyPagingItems()

	ListThanksScreen(
		listThanksRecordUiModels = listThanksRecordUiModels,
		onNewThanksClick = onNewThanksClick,
		onThanksClick = onThanksClick
	)
}

// stateless
@Composable
fun ListThanksScreen(
	listThanksRecordUiModels: LazyPagingItems<ListThanksRecordUiState>,
	onNewThanksClick: () -> Unit = {},
	onThanksClick: (ThanksRecordWithImages) -> Unit = {}
) {
	val scaffoldState = rememberCollapsingToolbarScaffoldState()

	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.background
	) {
		Box(modifier = Modifier.fillMaxSize()) {
			Image(
				painter = painterResource(id = R.drawable.main_background_1),
				contentScale = ContentScale.Crop,
				contentDescription = null,
				modifier = Modifier
					.fillMaxSize()
					.graphicsLayer {
						alpha = scaffoldState.toolbarState.progress
					}
			)

			CollapsingToolbarScaffold(
				modifier = Modifier.fillMaxSize(),
				state = scaffoldState,
				scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
				enabled = true,
				toolbar = {
					Spacer(
						modifier = Modifier
							.background(color = Color.Transparent)
							.fillMaxWidth()
							.height(0.dp)
					)

					Spacer(
						modifier = Modifier
							.background(color = Color.Transparent)
							.height(250.dp)
					)
				}
			) {
				val listThanksState = rememberLazyListState()

				ListThanks(
					listThanksRecordUiModels = listThanksRecordUiModels,
					lazyListState = listThanksState,
					onThanksClick = onThanksClick,
					modifier = Modifier
						.background(
							brush = Brush.verticalGradient(
								listOf(
									Color.Transparent,
									MaterialTheme.colors.surface.copy(
										alpha = scaffoldState.toolbarState.progress
									)
								)
							)
						)
						.fillMaxSize()
						.padding(horizontal = 20.dp)
				)

				AddThanksButton(
					onClick = onNewThanksClick,
					modifier = Modifier
						.padding(20.dp)
						.background(
							color = MaterialTheme.colors.primary,
							shape = CircleShape
						)
						.size(48.dp)
						.align(Alignment.BottomEnd)
				)
			}
		}
	}
}

@Composable
fun ListThanks(
	listThanksRecordUiModels: LazyPagingItems<ListThanksRecordUiState>,
	lazyListState: LazyListState,
	modifier: Modifier = Modifier,
	onThanksClick: (ThanksRecordWithImages) -> Unit = {}
) {
	LazyColumn(
		state = lazyListState,
		modifier = modifier,
		contentPadding = PaddingValues(top = 10.dp),
		verticalArrangement = Arrangement.spacedBy(5.dp)
	) {
		items(
			items = listThanksRecordUiModels,
			key = {
				when (it) {
					is ListThanksRecordUiState.DateHeaderItem -> it.date
					is ListThanksRecordUiState.ThanksRecordItemWithImages -> it.thanksRecordWithImages.thanksRecord.id
				}
			}
		) { item ->
			when (item) {
				is ListThanksRecordUiState.DateHeaderItem -> DateHeader(item.date)
				is ListThanksRecordUiState.ThanksRecordItemWithImages -> ThanksRecordListItem(
					thanksRecordWithImages = item.thanksRecordWithImages,
					onClick = onThanksClick
				)
				null -> { /* do nothing */
				}
			}
		}
	}
}

@Composable
private fun DateHeader(
	localDate: LocalDate,
	modifier: Modifier = Modifier
) {
	Surface(
		color = MaterialTheme.colors.surface.copy(alpha = 0.3f),
		shape = MaterialTheme.shapes.small,
		modifier = modifier.wrapContentSize()
	) {
		Text(
			text = localDate.toString(),
			style = MaterialTheme.typography.h4.copy(
				shadow = Shadow(
					color = Color.Gray.copy(alpha = 0.3f),
					offset = Offset(x = 2f, y = 4f),
					blurRadius = 0.1f
				)
			),
			modifier = Modifier.padding(5.dp)
		)
	}
}

@Preview
@Composable
private fun DateHeaderPreview() {
	DateHeader(localDate = LocalDate.of(2023, 1, 27))
}

@Composable
fun AddThanksButton(
	modifier: Modifier = Modifier,
	onClick: () -> Unit = {}
) {
	IconButton(
		onClick = onClick,
		modifier = modifier
	) {
		Icon(
			painter = painterResource(id = R.drawable.ic_add),
			contentDescription = "새로운 감사함을 기록하기",
			tint = MaterialTheme.colors.onPrimary,
			modifier = Modifier.size(28.dp)
		)
	}
}

@Composable
fun ThanksRecordListItem(
	thanksRecordWithImages: ThanksRecordWithImages,
	modifier: Modifier = Modifier,
	onClick: (ThanksRecordWithImages) -> Unit = {}
) {
	Surface(
		color = MaterialTheme.colors.surface.copy(alpha = 0.3f),
		shape = MaterialTheme.shapes.medium,
		modifier = modifier
			.clickable { onClick(thanksRecordWithImages) }
			.fillMaxWidth()
			.wrapContentHeight()
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.heightIn(min = 100.dp)
				.padding(horizontal = 16.dp, vertical = 5.dp)
		) {
			thanksRecordWithImages.attachedImageList.fistImage()?.let { attachedImage ->
				AsyncImage(
					model = attachedImage.imageUri,
					contentDescription = null,
					contentScale = ContentScale.Crop,
					modifier = Modifier.size(100.dp)
				)
			}

			Spacer(modifier = Modifier.width(10.dp))

			Column(
				modifier = Modifier
					.fillMaxWidth()
					.wrapContentHeight()
			) {
				Text(
					text = thanksRecordWithImages.thanksRecord.feeling.getResourceString(),
					style = MaterialTheme.typography.subtitle1.copy(
						shadow = Shadow(
							color = Color.Gray.copy(alpha = 0.3f),
							offset = Offset(x = 2f, y = 4f),
							blurRadius = 0.1f
						)
					),
					modifier = Modifier.fillMaxWidth()
				)

				Text(
					text = thanksRecordWithImages.thanksRecord.date.format(DateTimeFormatter.ISO_DATE),
					style = MaterialTheme.typography.subtitle2.copy(
						shadow = Shadow(
							color = Color.Gray.copy(alpha = 0.3f),
							offset = Offset(x = 2f, y = 4f),
							blurRadius = 0.1f
						)
					),
					modifier = Modifier.fillMaxWidth()
				)

				Text(
					text = thanksRecordWithImages.thanksRecord.thanksContent,
					style = MaterialTheme.typography.body1.copy(
						shadow = Shadow(
							color = Color.Gray.copy(alpha = 0.3f),
							offset = Offset(x = 2f, y = 4f),
							blurRadius = 0.1f
						)
					),
					maxLines = 3,
					overflow = TextOverflow.Ellipsis,
					modifier = Modifier.fillMaxWidth()
				)
			}
		}
	}
}

@Preview
@Composable
fun ThanksRecordListItemPreview() {
	val dummyThanksRecord = ThanksRecord(
		id = 1,
		feeling = Feeling.Thanks,
		thanksContent = """오늘도 건강할 수 있어서 감사합니다. 좋은 회사에서 일할 수 있어서 감사합니다. 좋은 사람들과 함깨 있어서 감사합니다""",
		date = LocalDate.now()
	)
	val dummyThanksRecordWithImages = ThanksRecordWithImages(
		thanksRecord = dummyThanksRecord,
		attachedImageList = AttachedImageList.emptyAttachedImageList()
	)

	ThanksRecordListItem(
		modifier = Modifier.fillMaxWidth(),
		thanksRecordWithImages = dummyThanksRecordWithImages
	)
}

@Preview
@Composable
fun ListThanksDashboardScreenPreview() {
//	SeizeTheDayTheme {
//		val state = ListThanksScreenState(
//			thanksRecordsMap = ThanksRecordsMap(
//				groupedThanksRecordByDate = listOf(
//					ThanksRecord(1, Feeling.Thanks, "오늘도 감사합니다.", LocalDate.now()),
//					ThanksRecord(2, Feeling.Joy, "오늘도 즐겁습니다.", LocalDate.now()),
//					ThanksRecord(3, Feeling.Awe, "오늘도 경의롭습니다.", LocalDate.now()),
//					ThanksRecord(4, Feeling.Happy, "오늘도 행복합니다.", LocalDate.now()),
//					ThanksRecord(5, Feeling.Hope, "오늘도 희망찹니다.", LocalDate.now()),
//				).groupBy { it.date }
//			)
//		)
//
//		ListThanksScreen(state, listThanksRecordUiModels = listThanksRecordUiModels)
//	}
}