package com.jingom.seizetheday.core.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jingom.seizetheday.presentation.ui.theme.SeizeTheDayTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocalImagePickerActivity: ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			SeizeTheDayTheme {
				LocalImagePickerScreen()
			}
		}
	}
}