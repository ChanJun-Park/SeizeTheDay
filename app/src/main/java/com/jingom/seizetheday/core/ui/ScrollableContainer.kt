package com.jingom.seizetheday.core.ui

import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ScrollableContainer(
	bodyContent: @Composable () -> Unit
) {
	val verticalScrollState = rememberScrollState()
	Column(
		modifier = Modifier
			.fillMaxSize()
			.verticalScroll(
				state = verticalScrollState,
				flingBehavior = ScrollableDefaults.flingBehavior()
			)
	) {
		bodyContent()
	}
}