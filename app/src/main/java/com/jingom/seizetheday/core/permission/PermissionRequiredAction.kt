package com.jingom.seizetheday.core.permission

import android.os.Build

sealed interface PermissionRequiredAction {
	fun getRequiredPermissions(): List<String>

	object ReadMediaImage: PermissionRequiredAction {
		override fun getRequiredPermissions(): List<String> {
			return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
				listOf(android.Manifest.permission.READ_MEDIA_IMAGES)
			} else {
				listOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
			}
		}
	}
}