package com.jingom.seizetheday.core.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.jingom.seizetheday.presentation.ui.theme.SeizeTheDayTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocalImagePickerActivity: ComponentActivity() {

	private val viewModel: LocalImagePickerViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			SeizeTheDayTheme {
				LocalImagePickerScreen(
					onBackButtonClick = this::onBackButtonClick,
					onAttachButtonClick = this::dispatchResultAndFinish
				)
			}
		}
	}

	private fun onBackButtonClick() {
		finish()
	}

	private fun dispatchResultAndFinish() {
		val selectedImages = viewModel.selectedImageList.value
		val resultIntent = Intent().putExtra(RESULT_KEY, selectedImages)
		setResult(RESULT_OK, resultIntent)
		finish()
	}

	companion object {
		const val RESULT_KEY = "resultKey"
		fun getIntent(context: Context) = Intent(context, LocalImagePickerActivity::class.java)
	}
}