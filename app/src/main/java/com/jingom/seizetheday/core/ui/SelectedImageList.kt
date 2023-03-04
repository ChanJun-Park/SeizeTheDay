package com.jingom.seizetheday.core.ui

import com.jingom.seizetheday.domain.model.media.MediaImage

class SelectedImageList(
	private val selectedImages: List<MediaImage> = emptyList()
) {
	fun add(image: MediaImage): SelectedImageList {
		return SelectedImageList(selectedImages + image)
	}

	fun remove(image: MediaImage): SelectedImageList {
		return SelectedImageList(selectedImages - image)
	}

	fun contains(image: MediaImage): Boolean {
		return selectedImages.contains(image)
	}

	fun indexOf(image: MediaImage): Int {
		return selectedImages.indexOf(image)
	}
}