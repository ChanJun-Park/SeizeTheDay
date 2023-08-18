package com.jingom.seizetheday.presentation.write

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.jingom.seizetheday.R
import com.jingom.seizetheday.core.permission.PermissionRequiredAction
import com.jingom.seizetheday.core.ui.LocalImagePickerActivity
import com.jingom.seizetheday.core.ui.SelectedImageList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val TEMP_MAX_IMAGE_PICK_COUNT = 10

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberWritingThanksContentElementState(
	scaffoldState: ScaffoldState = rememberScaffoldState(),
	coroutineScope: CoroutineScope = rememberCoroutineScope(),
	onImagesAttached: (SelectedImageList) -> Unit = {},
	imagePickerLauncher: ManagedActivityResultLauncher<LocalImagePickerActivity.ImagePickOptions, SelectedImageList> = rememberLauncherForActivityResult(
		contract = LocalImagePickerActivity.PickLocalImages(),
		onResult = onImagesAttached
	)
): WritingThanksContentElementState {

	val imagePermissionMessage = stringResource(R.string.media_image_permission_rationale)
	val mediaPermissionState: MultiplePermissionsState = rememberMultiplePermissionsState(
		permissions = PermissionRequiredAction.ReadMediaImage.getRequiredPermissions(),
		onPermissionsResult = { resultMap ->
			if (resultMap.allPermissionGranted()) {
				imagePickerLauncher.launch(LocalImagePickerActivity.ImagePickOptions(TEMP_MAX_IMAGE_PICK_COUNT))
			} else {
				coroutineScope.launch {
					scaffoldState.snackbarHostState.showSnackbar(imagePermissionMessage)
				}
			}
		}
	)

	return remember(scaffoldState, coroutineScope, onImagesAttached, imagePickerLauncher, mediaPermissionState) {

		WritingThanksContentElementState(
			scaffoldState = scaffoldState,
			mediaPermissionState = mediaPermissionState,
			imagePickerLauncher = imagePickerLauncher
		)
	}
}

@OptIn(ExperimentalPermissionsApi::class)
class WritingThanksContentElementState(
	val scaffoldState: ScaffoldState,
	private val mediaPermissionState: MultiplePermissionsState,
	private val imagePickerLauncher: ManagedActivityResultLauncher<LocalImagePickerActivity.ImagePickOptions, SelectedImageList>
) {
	var needToShowPermissionAlert by mutableStateOf(false)
		private set

	val mediaPermissionGranted get() = mediaPermissionState.allPermissionsGranted
	val shouldShowMediaPermissionRationale get() = mediaPermissionState.shouldShowRationale
	fun showImagePicker() = imagePickerLauncher.launch(LocalImagePickerActivity.ImagePickOptions(TEMP_MAX_IMAGE_PICK_COUNT))
	fun showMediaPermissionRequestAlert() {
		needToShowPermissionAlert = true
	}

	fun hideMediaPermissionRequestAlert() {
		needToShowPermissionAlert = false
	}

	fun requestMediaPermission() {
		mediaPermissionState.launchMultiplePermissionRequest()
	}
}