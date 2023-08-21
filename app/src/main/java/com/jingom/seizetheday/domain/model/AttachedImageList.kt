package com.jingom.seizetheday.domain.model

data class AttachedImageList(
	private val imageList: List<AttachedImage>
) {
	val size: Int
		get() = imageList.size
	fun forEachImage(action: (AttachedImage) -> Unit) {
		imageList.forEach(action)
	}

	fun fistImage(): AttachedImage? = imageList.firstOrNull()

	fun getImageList(): List<AttachedImage> = imageList

	companion object {
		fun emptyAttachedImageList() = AttachedImageList(emptyList())
	}
}