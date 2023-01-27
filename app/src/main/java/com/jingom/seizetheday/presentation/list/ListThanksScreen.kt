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
import androidx.compose.ui.draw.clip
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

data class ListThanksScreenState(
	val thanksRecordsMap: ThanksRecordsMap
)

// stateful
@Composable
fun ListThanksScreen(
	viewModel: ListThanksViewModel = hiltViewModel(),
	onNewThanksClick: () -> Unit = {},
	onThanksClick: (ThanksRecord) -> Unit = {}
) {
	val state = viewModel.thanksRecords.collectAsState().value

	ListThanksScreen(
		state = state,
		onNewThanksClick = onNewThanksClick,
		onThanksClick = onThanksClick
	)
}

// stateless
@Composable
fun ListThanksScreen(
	state: ListThanksScreenState,
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
					thanksRecordsMap = state.thanksRecordsMap,
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
	thanksRecordsMap: ThanksRecordsMap,
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
		thanksRecordsMap.forEachDateThanksRecords { localDate, thanksRecords ->

			stickyHeader(
				key = localDate,
				contentType = LocalDate::class
			) {
				DateHeader(localDate)
			}

			items(
				items = thanksRecords,
				key = { it.id },
				contentType = { ThanksRecord::class }
			) { item ->
				ThanksRecordListItem(
					thanksRecord = item,
					onClick = onThanksClick
				)
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
			// todo 이미지로 대체
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
			thanksContent = """오늘도 건강할 수 있어서 감사합니다. 좋은 회사에서 일할 수 있어서 감사합니다. 좋은 사람들과 함깨 있어서 감사합니다""",
			date = LocalDate.now()
		)
	)
}

@Preview
@Composable
fun ListThanksDashboardScreenPreview() {
	SeizeTheDayTheme {
		val state = ListThanksScreenState(
			thanksRecordsMap = ThanksRecordsMap(
				groupedThanksRecordByDate = listOf(
					ThanksRecord(1, Feeling.Thanks, "오늘도 감사합니다.", LocalDate.now()),
					ThanksRecord(2, Feeling.Joy, "오늘도 즐겁습니다.", LocalDate.now()),
					ThanksRecord(3, Feeling.Awe, "오늘도 경의롭습니다.", LocalDate.now()),
					ThanksRecord(4, Feeling.Happy, "오늘도 행복합니다.", LocalDate.now()),
					ThanksRecord(5, Feeling.Hope, "오늘도 희망찹니다.", LocalDate.now()),
				).groupBy { it.date }
			)
		)

		ListThanksScreen(state)
	}
}