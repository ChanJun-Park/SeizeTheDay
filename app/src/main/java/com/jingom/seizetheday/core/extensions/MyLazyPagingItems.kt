package com.jingom.seizetheday.core.extensions

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems

fun <T : Any> LazyGridScope.items(
	items: LazyPagingItems<T>,
	span: (LazyGridItemSpanScope.(index: Int) -> GridItemSpan)? = null,
	key: ((item: T) -> Any)? = null,
	itemContent: @Composable LazyGridItemScope.(value: T?) -> Unit
) {
	items(
		count = items.itemCount,
		span = span,
		key = if (key == null) null else { index ->
			val item = items.peek(index)
			if (item == null) {
				PagingPlaceholderKey(index)
			} else {
				key(item)
			}
		}
	) { index ->
		itemContent(items[index])
	}
}

@SuppressLint("BanParcelableUsage")
private data class PagingPlaceholderKey(private val index: Int) : Parcelable {
	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeInt(index)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object {
		@Suppress("unused")
		@JvmField
		val CREATOR: Parcelable.Creator<PagingPlaceholderKey> =
			object : Parcelable.Creator<PagingPlaceholderKey> {
				override fun createFromParcel(parcel: Parcel) =
					PagingPlaceholderKey(parcel.readInt())

				override fun newArray(size: Int) = arrayOfNulls<PagingPlaceholderKey?>(size)
			}
	}
}
