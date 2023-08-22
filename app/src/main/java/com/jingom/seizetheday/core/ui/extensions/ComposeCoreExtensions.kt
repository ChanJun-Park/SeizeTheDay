package com.jingom.seizetheday.core.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode

val isInspectionMode: Boolean
	@Composable get() = (LocalInspectionMode.current)