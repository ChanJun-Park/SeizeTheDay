package com.jingom.seizetheday.presentation.ui.theme

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp

@Preview(name = "light", uiMode = UI_MODE_NIGHT_NO, showBackground = true, showSystemUi = true)
@Preview(name = "night", uiMode = UI_MODE_NIGHT_YES, showBackground = true, showSystemUi = true)
@Composable
fun TestScreen() {
	SeizeTheDayTheme {
		val scaffoldState = rememberScaffoldState()

		Scaffold(
			scaffoldState = scaffoldState,
			topBar = {
				Surface(color = MaterialTheme.colors.primary) {
					Row(
						verticalAlignment = Alignment.CenterVertically,
						modifier = Modifier
							.fillMaxWidth()
							.height(60.dp)
							.padding(start = 10.dp)
					) {
						Text(
							text = "Title",
							style = MaterialTheme.typography.h4
						)
					}
				}
			},
			floatingActionButton = {
				FloatingActionButton(onClick = { /*TODO*/ }) {
					Icon(
						imageVector = Icons.Filled.Add,
						contentDescription = "",
						tint = MaterialTheme.colors.onSecondary
					)
				}
			}
		) {
			Surface(
				color = MaterialTheme.colors.background,
				modifier = Modifier
					.padding(it)
					.fillMaxSize()
			) {
				Column(modifier = Modifier.fillMaxSize()) {
					TestCard()
					TestCard()
					TestCard()
				}
			}
		}
	}
}

@Composable
fun TestCard() {
	Card(
		modifier = Modifier
			.padding(10.dp)
			.fillMaxWidth()
			.wrapContentHeight()
	) {
		val testMessage = remember {
			LoremIpsum(50).values.run {
				this.first()
			}
		}
		Text(
			text = testMessage,
			modifier = Modifier.padding(10.dp)
		)
	}
}