package com.jingom.seizetheday.core.ui

import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun VerticalScrollableContainer(
	modifier: Modifier = Modifier,
	horizontalAlignment: Alignment.Horizontal = Alignment.Start,
	verticalArrangement: Arrangement.Vertical = Arrangement.Top,
	bodyContent: @Composable () -> Unit = {}
) {
	val verticalScrollState = rememberScrollState()
	Column(
		horizontalAlignment = horizontalAlignment,
		verticalArrangement = verticalArrangement,
		modifier = modifier
			.fillMaxSize()
			.verticalScroll(
				state = verticalScrollState,
				flingBehavior = ScrollableDefaults.flingBehavior()
			)
	) {
		bodyContent()
	}
}