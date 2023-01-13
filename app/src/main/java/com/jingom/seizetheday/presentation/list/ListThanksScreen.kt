package com.jingom.seizetheday.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jingom.seizetheday.R
import com.jingom.seizetheday.domain.model.Feeling
import com.jingom.seizetheday.domain.model.ThanksRecord

data class ListThanksScreenState(
	val thanksRecords: List<ThanksRecord>
)

// stateful
@Composable
fun ListThanksScreen(
	viewModel: ListThanksViewModel = hiltViewModel(),
	onNewThanksClick: () -> Unit = {}
) {
	val state = viewModel.thanksRecords.collectAsState().value

	ListThanksScreen(
		state = state,
		onNewThanksClick = onNewThanksClick
	)
}

// stateless
@Composable
fun ListThanksScreen(
	state: ListThanksScreenState,
	onNewThanksClick: () -> Unit = {}
) {
	Surface(modifier = Modifier.fillMaxSize()) {
		Box(modifier = Modifier.fillMaxSize()) {
			LazyColumn(modifier = Modifier.fillMaxSize()) {
				items(
					items = state.thanksRecords,
					key = { it.id }
				) { item ->
					ThanksRecordListItem(
						modifier = Modifier
							.fillMaxWidth()
							.wrapContentHeight(),
						thanksRecord = item
					)
				}
			}

			IconButton(
				onClick = onNewThanksClick,
				modifier = Modifier
					.padding(20.dp)
					.background(
						color = MaterialTheme.colors.primary,
						shape = CircleShape
					)
					.size(48.dp)
					.align(Alignment.BottomEnd)
			) {
				Icon(
					painter = painterResource(id = R.drawable.ic_add),
					contentDescription = "새로운 감사함을 기록하기",
					tint = MaterialTheme.colors.onPrimary,
					modifier = Modifier.size(28.dp)
				)
			}
		}
	}
}

@Composable
fun ThanksRecordListItem(
	modifier: Modifier = Modifier,
	thanksRecord: ThanksRecord
) {
	Surface(modifier = modifier) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically
		) {
			Text(
				modifier = Modifier.requiredWidth(150.dp),
				text = thanksRecord.feeling.name,
				style = MaterialTheme.typography.h4
			)

			Spacer(modifier = Modifier.width(10.dp))

			Text(
				modifier = Modifier.fillMaxWidth(),
				text = thanksRecord.thanksContent,
				style = MaterialTheme.typography.body1,
				maxLines = 3,
				overflow = TextOverflow.Ellipsis
			)
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
			thanksContent = """오늘도 건강할 수 있어서 감사합니다. 좋은 회사에서 일할 수 있어서 감사합니다. 좋은 사람들과 함깨 있어서 감사합니다"""
		)
	)
}

@Preview
@Composable
fun ListThanksDashboardScreenPreview() {
	val state = ListThanksScreenState(
		thanksRecords = listOf(
			ThanksRecord(1, Feeling.Thanks, "오늘도 감사합니다."),
			ThanksRecord(2, Feeling.Joy, "오늘도 즐겁습니다."),
			ThanksRecord(3, Feeling.Awe, "오늘도 경의롭습니다."),
			ThanksRecord(4, Feeling.Happy, "오늘도 행복합니다."),
			ThanksRecord(5, Feeling.Hope, "오늘도 희망찹니다."),
		)
	)

	ListThanksScreen(state)
}