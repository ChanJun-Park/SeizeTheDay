package com.jingom.seizetheday.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jingom.seizetheday.domain.LocalMediaImageLoader
import com.jingom.seizetheday.domain.model.media.MediaImage
import com.jingom.seizetheday.domain.model.media.MediaImageAlbum
import com.jingom.seizetheday.domain.usecase.GetMediaImageAlbumsUseCase
import com.jingom.seizetheday.domain.usecase.GetMediaImagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalImagePickerViewModel @Inject constructor(
	private val getMediaImages: GetMediaImagesUseCase,
	private val getMediaImageAlbums: GetMediaImageAlbumsUseCase
) : ViewModel() {
	private val _selectedImageList = MutableStateFlow(SelectedImageList())

	private val _selectedImageAlbum = MutableStateFlow(MediaImageAlbum(LocalMediaImageLoader.ALL_IMAGES_ALBUM_ID))
	val selectedImageAlbum: StateFlow<MediaImageAlbum> = _selectedImageAlbum

	private val _imageListUiState = MutableStateFlow(ImageListUiState())
	val imageListUiState: StateFlow<ImageListUiState> = _imageListUiState

	private val _albumListUiState = MutableStateFlow(AlbumListUiState())
	val albumListUiState: StateFlow<AlbumListUiState> = _albumListUiState

	private val _isAlbumListVisible = MutableStateFlow(false)
	val isAlbumListVisible: StateFlow<Boolean> = _isAlbumListVisible

	private var imageListLoadingJob: Job? = null
	private var imageAlbumListLoadingJob: Job? = null

	init {
		loadMediaImages()
		loadMediaImageAlbums()

		collectSelectedImageState()
	}

	fun toggleImageSelectionState(image: MediaImage) = _selectedImageList.update { currentSelectedImageList ->
		if (currentSelectedImageList.contains(image)) {
			currentSelectedImageList.remove(image)
		} else {
			currentSelectedImageList.add(image)
		}
	}

	fun changeSelectedAlbum(album: MediaImageAlbum) {
		_selectedImageAlbum.value = album
		loadMediaImages(album.albumId)
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
}