package com.jingom.seizetheday.core.ui

import android.content.Context
import android.content.Intent
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
				LocalImagePickerScreen(onBackButtonClick = this::onBackButtonClick)
			}
		}
	}

	private fun onBackButtonClick() {
		finish()

}
	companion object {
		fun getIntent(context: Context) = Intent(context, LocalImagePickerActivity::class.java)
	}
}