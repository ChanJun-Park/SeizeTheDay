package com.jingom.seizetheday.core.extensions

import android.database.Cursor

fun <T> Cursor?.toData(action: (Cursor) -> T): T? = use { cursor ->
	if (cursor != null && cursor.moveToNext()) {
		return action(cursor)
	}
	return null
}

fun <T> Cursor?.toDataList(action: (Cursor) -> T): List<T> = use { cursor ->
	val list = mutableListOf<T>()

	if (cursor != null && cursor.moveToNext()) {
		do {
			list.add(action(cursor))
		} while (cursor.moveToNext())
	}

	return list
}