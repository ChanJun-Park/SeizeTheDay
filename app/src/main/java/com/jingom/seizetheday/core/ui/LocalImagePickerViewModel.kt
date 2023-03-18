package com.jingom.seizetheday.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jingom.seizetheday.R
import com.jingom.seizetheday.core.UIText
import com.jingom.seizetheday.domain.LocalMediaImageLoader
import com.jingom.seizetheday.domain.model.media.MediaImage
import com.jingom.seizetheday.domain.model.media.MediaImageAlbum
import com.jingom.seizetheday.domain.usecase.GetMediaImageAlbumsUseCase
import com.jingom.seizetheday.domain.usecase.GetMediaImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalImagePickerViewModel @Inject constructor(
	private val getMediaImages: GetMediaImagesUseCase,
	private val getMediaImageAlbums: GetMediaImageAlbumsUseCase
) : ViewModel() {
	private val _selectedImageList = MutableStateFlow(SelectedImageList())
	val selectedImageList = _selectedImageList.asStateFlow()

	private val _selectedImageAlbum = MutableStateFlow(MediaImageAlbum(LocalMediaImageLoader.ALL_IMAGES_ALBUM_ID))
	val selectedImageAlbum = _selectedImageAlbum.asStateFlow()

	private val _imageListUiState = MutableStateFlow(ImageListUiState())
	val imageListUiState = _imageListUiState.asStateFlow()

	private val _albumListUiState = MutableStateFlow(AlbumListUiState())
	val albumListUiState = _albumListUiState.asStateFlow()

	private val _errorMessages = MutableStateFlow<List<UIText>>(emptyList())
	val errorMessages = _errorMessages.asStateFlow()

	private var imageListLoadingJob: Job? = null
	private var imageAlbumListLoadingJob: Job? = null

	private var maxImagePickCount: Int? = null

	init {
		loadMediaImages()
		loadMediaImageAlbums()

		collectSelectedImageState()
	}

	fun toggleImageSelectionState(image: MediaImage) {
		val currentSelectedImageList = selectedImageList.value
		if (currentSelectedImageList.contains(image)) {
			removeImage(currentSelectedImageList, image)
		} else {
			addImage(currentSelectedImageList, image)
		}
	}

	fun changeSelectedAlbum(album: MediaImageAlbum) {
		_selectedImageAlbum.value = album
		loadMediaImages(album.albumId)
	}

	fun setImagePickOptions(imagePickOptions: LocalImagePickerActivity.ImagePickOptions?) {
		imagePickOptions ?: return

		maxImagePickCount = imagePickOptions.maxPickCount
	}

	fun shownErrorMessage(errorMessage: UIText) {
		val newErrorMessages = errorMessages.value - errorMessage
		_errorMessages.update { newErrorMessages }
	}

	private fun loadMediaImages(albumId: Int = LocalMediaImageLoader.ALL_IMAGES_ALBUM_ID) {
		imageListLoadingJob?.cancel()
		imageListLoadingJob = viewModelScope.launch {
			val images = getMediaImages(albumId)
			_imageListUiState.value = createImageListUiState(images)
		}
	}

	private fun createImageListUiState(images: List<MediaImage>) = ImageListUiState(
		imageList = images.map(this::toMediaImageUiState)
	)

	private fun toMediaImageUiState(mediaImage: MediaImage): MediaImageUiState {
		val currentlySelectedImageList = _selectedImageList.value

		return MediaImageUiState(
			mediaImage = mediaImage,
			isSelected = currentlySelectedImageList.contains(mediaImage),
			selectionNumber = currentlySelectedImageList.indexOf(mediaImage)
		)
	}

	private fun loadMediaImageAlbums() {
		imageAlbumListLoadingJob?.cancel()
		imageAlbumListLoadingJob = viewModelScope.launch {
			val imageAlbums = getMediaImageAlbums()
			_albumListUiState.value = createAlbumListUiState(imageAlbums)
		}
	}

	private fun createAlbumListUiState(imageAlbums: List<MediaImageAlbum>) = AlbumListUiState(
		albumList = imageAlbums.map(this::toMediaImageAlbumUiState)
	)

	private fun toMediaImageAlbumUiState(album: MediaImageAlbum) = MediaImageAlbumUiState(
		mediaImageAlbum = album,
		isSelected = selectedImageAlbum.value == album
	)

	private fun collectSelectedImageState() {
		viewModelScope.launch {
			_selectedImageList.collectLatest {
				_imageListUiState.value = createImageListUiState(
					imageListUiState.value.toMediaImageList()
				)
			}
		}
	}

	private fun ImageListUiState.toMediaImageList(): List<MediaImage> {
		return imageList.map { it.mediaImage }
	}

	private fun removeImage(currentSelectedImageList: SelectedImageList, image: MediaImage) {
		_selectedImageList.value = currentSelectedImageList.remove(image)
	}

	private fun addImage(currentSelectedImageList: SelectedImageList, image: MediaImage) {
		val maxImagePickCount = maxImagePickCount
		if (maxImagePickCount == null || currentSelectedImageList.size < maxImagePickCount) {
			_selectedImageList.value = currentSelectedImageList.add(image)
		} else {
			val newErrorMessages = errorMessages.value + UIText.StringResource(R.string.max_image_count_error, maxImagePickCount)
			_errorMessages.update { newErrorMessages }
		}
	}
}