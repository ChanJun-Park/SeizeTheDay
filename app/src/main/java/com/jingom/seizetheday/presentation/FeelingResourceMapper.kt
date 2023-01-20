package com.jingom.seizetheday.presentation

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.jingom.seizetheday.R
import com.jingom.seizetheday.domain.model.Feeling

@StringRes
private fun Feeling.getStringResourceId(): Int {
	return when (this) {
		Feeling.Happy -> R.string.happy
		Feeling.Awe -> R.string.awe
		Feeling.Hope -> R.string.hope
		Feeling.Joy -> R.string.joy
		Feeling.Pride -> R.string.pride
		Feeling.Serenity -> R.string.serenity
		Feeling.Thanks -> R.string.thanks
	}
}

@Composable
fun Feeling.getResourceString(): String {
	val context = LocalContext.current
	return context.resources.getString(this.getStringResourceId())
}