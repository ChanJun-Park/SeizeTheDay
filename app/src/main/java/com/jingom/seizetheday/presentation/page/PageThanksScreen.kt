package com.jingom.seizetheday.presentation.page

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.jingom.seizetheday.domain.model.Feeling
import com.jingom.seizetheday.domain.model.ThanksRecord
import com.jingom.seizetheday.presentation.getResourceString
import com.jingom.seizetheday.presentation.ui.theme.Red100
import com.jingom.seizetheday.presentation.ui.theme.Red50
import java.time.LocalDate

@Composable
fun PageThanksScreen() {
	Surface(
		modifier = Modifier.fillMaxSize(),
		color = MaterialTheme.colors.surface.copy(alpha = 0.1f)
	) {

	}
}

@Composable
private fun DayThanksPage(
	thanksRecord: ThanksRecord,
	modifier: Modifier = Modifier
) {
	Surface(
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

@Composable
private fun SelectedFeeling(feeling: Feeling) {
	Text(
		text = feeling.getResourceString(),
		style = MaterialTheme.typography.h5
	)
}