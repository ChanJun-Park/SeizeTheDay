package com.jingom.seizetheday.core.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.viewModels
import com.jingom.seizetheday.presentation.ui.theme.SeizeTheDayTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.parcelize.Parcelize

private const val KEY_IMAGE_PICK_OPTIONS = "keyImagePickerOptions"
private const val KEY_RESULT_IMAGE_PICK = "keyImagePick"

@AndroidEntryPoint
class LocalImagePickerActivity : ComponentActivity() {

	private val viewModel: LocalImagePickerViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		initImagePickOptions()

		setContent {
			SeizeTheDayTheme {
				LocalImagePickerScreen(
					onBackButtonClick = this::onBackButtonClick,
					onAttachButtonClick = this::dispatchResultAndFinish
				)
			}
		}
	}

	private fun initImagePickOptions() {
		val imagePickOptions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			intent.getParcelableExtra(KEY_IMAGE_PICK_OPTIONS, ImagePickOptions::class.java)
		} else {
			intent.getParcelableExtra(KEY_IMAGE_PICK_OPTIONS)
		}

		viewModel.setImagePickOptions(imagePickOptions)
	}

	private fun onBackButtonClick() {
		finish()
	}

	private fun dispatchResultAndFinish() {
		val selectedImages = viewModel.selectedImageList.value
		val resultIntent = Intent().putExtra(KEY_RESULT_IMAGE_PICK, selectedImages)
		setResult(RESULT_OK, resultIntent)
		finish()
	}

	@Parcelize
	data class ImagePickOptions(val maxPickCount: Int? = null) : Parcelable

	class PickLocalImages : ActivityResultContract<ImagePickOptions, SelectedImageList>() {
		override fun createIntent(context: Context, input: ImagePickOptions): Intent {
			return Intent(context, LocalImagePickerActivity::class.java).apply {
				putExtra(KEY_IMAGE_PICK_OPTIONS, input)
			}
		}

		override fun parseResult(resultCode: Int, intent: Intent?): SelectedImageList =
			if (resultCode == Activity.RESULT_OK && intent != null) {
				getResult(intent) ?: SelectedImageList()
			} else {
				SelectedImageList()
			}

		private fun getResult(intent: Intent): SelectedImageList? =
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
				intent.getParcelableExtra(KEY_RESULT_IMAGE_PICK, SelectedImageList::class.java)
			} else {
				intent.getParcelableExtra(KEY_RESULT_IMAGE_PICK)
			}
	}

}