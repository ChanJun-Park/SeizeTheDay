package com.jingom.seizetheday.presentation.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.jingom.seizetheday.R
import com.jingom.seizetheday.core.extensions.items
import com.jingom.seizetheday.core.time.DateTimeFormatters
import com.jingom.seizetheday.domain.model.AttachedImageList
import com.jingom.seizetheday.domain.model.Feeling
import com.jingom.seizetheday.domain.model.ThanksRecord
import com.jingom.seizetheday.domain.model.ThanksRecordWithImages
import com.jingom.seizetheday.presentation.getResourceString
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ExperimentalToolbarApi
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
@OptIn(ExperimentalToolbarApi::class)
@Composable
fun ListThanksScreen(
	listThanksRecordUiModels: LazyPagingItems<ListThanksRecordUiState>,
	onNewThanksClick: () -> Unit = {},
	onThanksClick: (ThanksRecordWithImages) -> Unit = {}
) {
	val scaffoldState = rememberCollapsingToolbarScaffoldState()
	val lazyGridState = rememberLazyGridState()
	var viewTypeState by rememberSaveable { mutableStateOf(ListThanksViewType.ContentWithMiniThumbnail) }

	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.background
	) {
		Box(modifier = Modifier.fillMaxSize()) {
			Image(
				painter = painterResource(id = R.drawable.main_background_9),
				contentScale = ContentScale.Crop,
				contentDescription = null,
				modifier = Modifier
					.fillMaxSize()
			)

			CollapsingToolbarScaffold(
				modifier = Modifier.fillMaxSize(),
				state = scaffoldState,
				scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
				enabled = true,
				toolbar = {
					// Collapsing toolbar collapses its size as small as the that of
					// a smallest child. To make the toolbar collapse to 0dp, we create
					// a dummy Spacer composable.
					Spacer(
						modifier = Modifier
							.background(color = Color.Transparent)
							.fillMaxWidth()
							.height(0.dp)
					)

					// Collapsing toolbar expands its size as large as the that of
					// a largest child. To make the toolbar expand to maxToolbarHeight, we create
					// a dummy Spacer composable.
					val maxToolbarHeight = 400.dp
					Spacer(
						modifier = Modifier
							.offset(y = maxToolbarHeight * (scaffoldState.toolbarState.progress - 1))
							.background(
								brush = Brush.verticalGradient(
									colors = listOf(
										Color.Transparent,
										MaterialTheme.colors.surface.copy(
											alpha = 0.9f
										)
									),
									startY = 70f
								)
							)
							.fillMaxWidth()
							.height(maxToolbarHeight)
					)
				}
			) {
				ListThanks(
					listThanksRecordUiModels = listThanksRecordUiModels,
					lazyGridState = lazyGridState,
					listThanksViewType = viewTypeState,
					onThanksClick = onThanksClick,
					onClickThumbnailTypeButton = {
						viewTypeState = ListThanksViewType.Thumbnail
					},
					onClickContentWithMiniThumbnailButton = {
						viewTypeState = ListThanksViewType.ContentWithMiniThumbnail
					},
					onClickContentWithBigThumbnailButton = {
						viewTypeState = ListThanksViewType.ContentWithBigThumbnail
					},
					modifier = Modifier
						.background(
							color = MaterialTheme.colors.surface.copy(
								alpha = 0.9f
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
	lazyGridState: LazyGridState,
	modifier: Modifier = Modifier,
	listThanksViewType: ListThanksViewType = ListThanksViewType.ContentWithBigThumbnail,
	onThanksClick: (ThanksRecordWithImages) -> Unit = {},
	onClickThumbnailTypeButton: () -> Unit = {},
	onClickContentWithMiniThumbnailButton: () -> Unit = {},
	onClickContentWithBigThumbnailButton: () -> Unit = {}
) {
	LazyVerticalGrid(
		state = lazyGridState,
		columns = listThanksViewType.gridCells(),
		contentPadding = PaddingValues(top = 10.dp),
		verticalArrangement = Arrangement.spacedBy(5.dp),
		horizontalArrangement = Arrangement.spacedBy(5.dp),
		modifier = modifier,
	) {
		item(
			key = "view type button",
			span = { GridItemSpan(maxLineSpan) }
		) {
			ListThanksViewTypeButtons(
				listThanksViewType = listThanksViewType,
				onClickThumbnailTypeButton = onClickThumbnailTypeButton,
				onClickContentWithMiniThumbnailButton = onClickContentWithMiniThumbnailButton,
				onClickContentWithBigThumbnailButton = onClickContentWithBigThumbnailButton
			)
		}

		items(
			items = listThanksRecordUiModels,
			span = getItemSpanStrategy(listThanksRecordUiModels),
			key = getItemKeyStrategy()
		) { item ->
			when (item) {
				is ListThanksRecordUiState.DateHeaderItem -> DateHeader(item.date)
				is ListThanksRecordUiState.ThanksRecordItemWithImages -> ThanksRecordListItem(
					thanksRecordWithImages = item.thanksRecordWithImages,
					onClick = onThanksClick,
					listThanksViewType = listThanksViewType
				)
				null -> {
					/* do nothing */
				}
			}
		}
	}
}

@Composable
fun ListThanksViewTypeButtons(
	listThanksViewType: ListThanksViewType,
	onClickThumbnailTypeButton: () -> Unit = {},
	onClickContentWithMiniThumbnailButton: () -> Unit = {},
	onClickContentWithBigThumbnailButton: () -> Unit = {}
) {
	Row(
		horizontalArrangement = Arrangement.End,
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.padding(vertical = 3.dp)
			.fillMaxWidth()
	) {
		Row {
			IconButton(onClick = onClickThumbnailTypeButton) {
				Icon(
					painter = painterResource(R.drawable.ic_grid),
					contentDescription = "view in grid form",
					tint = if (listThanksViewType == ListThanksViewType.Thumbnail) {
						Color.Black
					} else {
						Color.Black.copy(alpha = 0.3f)
					},
					modifier = Modifier.size(30.dp)
				)
			}

			IconButton(onClick = onClickContentWithMiniThumbnailButton) {
				Icon(
					painter = painterResource(R.drawable.ic_content_with_mini_thumbnail),
					contentDescription = "view in content with mini thumbnail form",
					tint = if (listThanksViewType == ListThanksViewType.ContentWithMiniThumbnail) {
						Color.Black
					} else {
						Color.Black.copy(alpha = 0.3f)
					},
					modifier = Modifier.size(30.dp)
				)
			}

			IconButton(onClick = onClickContentWithBigThumbnailButton) {
				Icon(
					painter = painterResource(R.drawable.ic_content_wth_big_thumbnail),
					contentDescription = "view in content with big form",
					tint = if (listThanksViewType == ListThanksViewType.ContentWithBigThumbnail) {
						Color.Black
					} else {
						Color.Black.copy(alpha = 0.3f)
					},
					modifier = Modifier.size(30.dp)
				)
			}
		}
	}
}

private fun getItemSpanStrategy(
	listThanksRecordUiModels: LazyPagingItems<ListThanksRecordUiState>
): LazyGridItemSpanScope.(index: Int) -> GridItemSpan = { index ->
	when (listThanksRecordUiModels.peek(index)) {
		is ListThanksRecordUiState.DateHeaderItem -> GridItemSpan(maxLineSpan)
		else -> GridItemSpan(1)
	}
}

private fun getItemKeyStrategy(): (item: ListThanksRecordUiState) -> Any = {
	when (it) {
		is ListThanksRecordUiState.DateHeaderItem -> "DateHeaderItem ${it.date}"
		is ListThanksRecordUiState.ThanksRecordItemWithImages -> "ThanksRecordItem ${it.thanksRecordWithImages.thanksRecord.id}"
	}
}

fun ListThanksViewType.gridCells() = when (this) {
	ListThanksViewType.Thumbnail -> GridCells.Adaptive(100.dp)
	ListThanksViewType.ContentWithMiniThumbnail,
	ListThanksViewType.ContentWithBigThumbnail -> GridCells.Fixed(1)
}

@Composable
private fun DateHeader(localDate: LocalDate) {
	Row(Modifier.fillMaxWidth()) {
		Text(
			text = localDate.toString(),
			style = MaterialTheme.typography.h4.copy(
				shadow = Shadow(
					color = Color.Gray.copy(alpha = 0.3f),
					offset = Offset(x = 2f, y = 4f),
					blurRadius = 0.1f
				)
			),
			modifier = Modifier
				.background(
					color = MaterialTheme.colors.surface.copy(alpha = 0.3f),
					shape = MaterialTheme.shapes.small
				)
				.wrapContentSize()
				.padding(5.dp)
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
	onClick: (ThanksRecordWithImages) -> Unit = {},
	listThanksViewType: ListThanksViewType = ListThanksViewType.ContentWithMiniThumbnail
) {
	when (listThanksViewType) {
		ListThanksViewType.Thumbnail -> Thumbnail(
			thanksRecordWithImages = thanksRecordWithImages,
			onClick = onClick
		)

		ListThanksViewType.ContentWithMiniThumbnail -> ContentWithMiniThumbnail(
			thanksRecordWithImages = thanksRecordWithImages,
			onClick = onClick
		)

		ListThanksViewType.ContentWithBigThumbnail -> ContentWithBigThumbnail(
			thanksRecordWithImages = thanksRecordWithImages,
			onClick = onClick
		)
	}
}

@Composable
private fun Thumbnail(
	thanksRecordWithImages: ThanksRecordWithImages,
	modifier: Modifier = Modifier,
	onClick: (ThanksRecordWithImages) -> Unit = {}
) {
	val thumbnailImage = thanksRecordWithImages.attachedImageList.fistImage()
	val localizedDateString = thanksRecordWithImages.thanksRecord.date.format(DateTimeFormatters.localizedDate)

	ThumbnailImage(
		model = thumbnailImage?.imageUri,
		contentDescription = stringResource(R.string.content_description_thumbnail_image, localizedDateString),
		modifier = modifier
			.fillMaxWidth()
			.aspectRatio(1f)
			.clickable { onClick(thanksRecordWithImages) },
		imageCount = thanksRecordWithImages.attachedImageList.size
	)
}

@Composable
private fun ContentWithMiniThumbnail(
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
				.padding(16.dp)
		) {
			thanksRecordWithImages.attachedImageList.fistImage()?.let { attachedImage ->
				AsyncImage(
					model = attachedImage.imageUri,
					contentDescription = null,
					contentScale = ContentScale.Crop,
					modifier = Modifier
						.size(100.dp)
						.clip(shape = RoundedCornerShape(size = 5.dp))
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

@Composable
private fun ContentWithBigThumbnail(
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
		Column(
			modifier = Modifier.fillMaxWidth()
		) {
			thanksRecordWithImages.attachedImageList.fistImage()?.let { attachedImage ->
				AsyncImage(
					model = attachedImage.imageUri,
					contentDescription = null,
					contentScale = ContentScale.Crop,
					modifier = Modifier
						.fillMaxWidth()
						.aspectRatio(ratio = 2f)
						.clip(shape = RoundedCornerShape(3.dp))
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

@Composable
private fun ThumbnailImage(
	model: Any?,
	contentDescription: String,
	modifier: Modifier = Modifier,
	imageCount: Int = 1
) {
	Box(modifier) {
		if (model != null) {
			AsyncImage(
				model = model,
				contentDescription = contentDescription,
				contentScale = ContentScale.Crop,
				modifier = Modifier.fillMaxSize()
			)
		} else {
			Image(
				painter = painterResource(R.drawable.main_background_9),
				contentDescription = contentDescription,
				contentScale = ContentScale.Crop,
				modifier = Modifier.fillMaxSize()
			)
		}

		if (imageCount != 0) {
			Text(
				text = imageCount.toString(),
				style = MaterialTheme.typography.caption.copy(
					color = Color.White
				),
				modifier = Modifier
					.padding(
						top = 5.dp,
						end = 5.dp
					)
					.align(
						alignment = Alignment.TopEnd
					)
					.background(
						color = Color.Black.copy(alpha = 0.5f),
						shape = RoundedCornerShape(percent = 50)
					)
					.padding(
						horizontal = 10.dp,
						vertical = 2.dp
					)
			)
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