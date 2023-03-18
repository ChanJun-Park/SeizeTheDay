package com.jingom.seizetheday.core.ui

import android.os.Parcelable
import com.jingom.seizetheday.domain.model.media.MediaImage
import kotlinx.parcelize.Parcelize

@Parcelize
class SelectedImageList(
	private val selectedImages: List<MediaImage> = emptyList()
): Parcelable {

	val size: Int
		get() = selectedImages.size

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

	fun <R> map(transform: (MediaImage) -> R): List<R> {
		return selectedImages.map(transform)
	}
}