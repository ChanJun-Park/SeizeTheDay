package com.jingom.seizetheday.presentation.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.jingom.seizetheday.R
import com.jingom.seizetheday.domain.model.Feeling
import com.jingom.seizetheday.domain.model.ThanksRecord
import com.jingom.seizetheday.domain.model.ThanksRecordsMap
import com.jingom.seizetheday.presentation.getResourceString
import com.jingom.seizetheday.presentation.ui.theme.SeizeTheDayTheme
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed interface ListThanksRecordUiModel {
	data class ThanksRecordItem(val thanksRecord: ThanksRecord): ListThanksRecordUiModel
	data class DateHeaderItem(val date: LocalDate): ListThanksRecordUiModel
}

// stateful
@Composable
fun ListThanksScreen(
	viewModel: ListThanksViewModel = hiltViewModel(),
	onNewThanksClick: () -> Unit = {},
	onThanksClick: (ThanksRecord) -> Unit = {}
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
	listThanksRecordUiModels: LazyPagingItems<ListThanksRecordUiModel>,
	onNewThanksClick: () -> Unit = {},
	onThanksClick: (ThanksRecord) -> Unit = {}
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListThanks(
	listThanksRecordUiModels: LazyPagingItems<ListThanksRecordUiModel>,
	lazyListState: LazyListState,
	modifier: Modifier = Modifier,
	onThanksClick: (ThanksRecord) -> Unit = {}
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
					is ListThanksRecordUiModel.DateHeaderItem -> it.date
					is ListThanksRecordUiModel.ThanksRecordItem -> it.thanksRecord.id
				}
			}
		) { item ->
			when (item) {
				is ListThanksRecordUiModel.DateHeaderItem -> DateHeader(item.date)
				is ListThanksRecordUiModel.ThanksRecordItem -> ThanksRecordListItem(
					thanksRecord = item.thanksRecord,
					onClick = onThanksClick
				)
				null -> { /* do nothing */ }
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
			contentDescription = "????????? ???????????? ????????????",
			tint = MaterialTheme.colors.onPrimary,
			modifier = Modifier.size(28.dp)
		)
	}
}

@Composable
fun ThanksRecordListItem(
	thanksRecord: ThanksRecord,
	modifier: Modifier = Modifier,
	onClick: (ThanksRecord) -> Unit = {}
) {
	Surface(
		color = MaterialTheme.colors.surface.copy(alpha = 0.3f),
		shape = MaterialTheme.shapes.medium,
		modifier = modifier
			.clickable { onClick(thanksRecord) }
			.fillMaxWidth()
			.wrapContentHeight()
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.heightIn(min = 100.dp)
				.padding(horizontal = 16.dp, vertical = 5.dp)
		) {
			// todo ???????????? ??????
			Image(
				painter = painterResource(id = R.drawable.main_background_2),
				contentDescription = null,
				contentScale = ContentScale.Crop,
				modifier = Modifier.size(100.dp)
			)

			Spacer(modifier = Modifier.width(10.dp))

			Column(
				modifier = Modifier
					.fillMaxWidth()
					.wrapContentHeight()
			) {
				Text(
					text = thanksRecord.feeling.getResourceString(),
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
					text = thanksRecord.date.format(DateTimeFormatter.ISO_DATE),
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
					text = thanksRecord.thanksContent,
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
	ThanksRecordListItem(
		modifier = Modifier.fillMaxWidth(),
		thanksRecord = ThanksRecord(
			id = 1,
			feeling = Feeling.Thanks,
			thanksContent = """????????? ????????? ??? ????????? ???????????????. ?????? ???????????? ?????? ??? ????????? ???????????????. ?????? ???????????? ?????? ????????? ???????????????""",
			date = LocalDate.now()
		)
	)
}

@Preview
@Composable
fun ListThanksDashboardScreenPreview() {
//	SeizeTheDayTheme {
//		val state = ListThanksScreenState(
//			thanksRecordsMap = ThanksRecordsMap(
//				groupedThanksRecordByDate = listOf(
//					ThanksRecord(1, Feeling.Thanks, "????????? ???????????????.", LocalDate.now()),
//					ThanksRecord(2, Feeling.Joy, "????????? ???????????????.", LocalDate.now()),
//					ThanksRecord(3, Feeling.Awe, "????????? ??????????????????.", LocalDate.now()),
//					ThanksRecord(4, Feeling.Happy, "????????? ???????????????.", LocalDate.now()),
//					ThanksRecord(5, Feeling.Hope, "????????? ???????????????.", LocalDate.now()),
//				).groupBy { it.date }
//			)
//		)
//
//		ListThanksScreen(state, listThanksRecordUiModels = listThanksRecordUiModels)
//	}
}