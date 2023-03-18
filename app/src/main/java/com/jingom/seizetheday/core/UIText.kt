package com.jingom.seizetheday.core

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

sealed class UIText {
	data class DynamicString(val value: String): UIText()

	class StringResource(
		@StringRes val resId: Int,
		vararg val args: Any
	): UIText() {

		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (other !is StringResource) return false
			return this.resId == other.resId && this.args.contentEquals(other.args)
		}

		override fun hashCode(): Int {
			var result = resId
			result = 31 * result + args.contentHashCode()
			return result
		}
	}

	fun asString(context: Context): String {
		return when(this) {
			is DynamicString -> value
			is StringResource -> context.getString(resId, *args)
		}
	}

	@Composable
	fun asString(): String {
		val context = LocalContext.current

		return asString(context)
	}
}