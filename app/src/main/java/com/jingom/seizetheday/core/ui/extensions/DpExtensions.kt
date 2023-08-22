package com.jingom.seizetheday.core.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Dp.dpToPx(): Float {
	val density = LocalDensity.current
	return with(density) {
		toPx()
	}
}