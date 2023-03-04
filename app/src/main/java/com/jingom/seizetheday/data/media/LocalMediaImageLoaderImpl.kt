package com.jingom.seizetheday.data.media

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.jingom.seizetheday.core.extensions.toData
import com.jingom.seizetheday.core.extensions.toDataList
import com.jingom.seizetheday.domain.model.media.MediaImage
import com.jingom.seizetheday.domain.model.media.MediaImageAlbum
import com.jingom.seizetheday.di.coroutine.IoDispatcher
import com.jingom.seizetheday.domain.LocalMediaImageLoader
import com.jingom.seizetheday.domain.LocalMediaImageLoader.Companion.ALL_IMAGES_ALBUM_ID
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class LocalMediaImageLoaderImpl(
	private val localMediaImageCursorLoader: LocalMediaImageCursorLoader,
	@IoDispatcher private val dispatcher: CoroutineDispatcher
): LocalMediaImageLoader {
	override suspend fun getLocalMedialImageAlbumList(): List<MediaImageAlbum> = withContext(dispatcher) {
		val albumList = mutableListOf<MediaImageAlbum>()
		val albumIdList = mutableListOf<Int>()

		getAllImageAlbum()?.let { allImageAlbum ->
			albumList.add(allImageAlbum)
		}

		getAlbumIdList().forEach { albumId ->
			if (albumIdList.contains(albumId).not()) {
				val imageAlbum = getImageAlbum(albumId) ?: return@forEach // continue

				albumIdList.add(albumId)
				albumList.add(imageAlbum)
			}
		}

		return@withContext albumList
	}

	override suspend fun getLocalMediaImageList(albumId: Int): List<MediaImage> = withContext(dispatcher) {
		localMediaImageCursorLoader.getMediaImagesCursor(albumId).toDataList { cursor ->
			getMediaImage(cursor)
		}
	}

	private fun getAllImageAlbum(): MediaImageAlbum? = localMediaImageCursorLoader.getMediaImagesWithAlbumNameCursor().toData { cursor ->
		val imageCount = cursor.count

		val thumbnailImage = getMediaImage(cursor)

		MediaImageAlbum(
			albumId = ALL_IMAGES_ALBUM_ID,
			albumName = "",
			thumbnailImage = thumbnailImage,
			imageCount = imageCount
		)
	}

	private fun getImageAlbum(albumId: Int): MediaImageAlbum? = localMediaImageCursorLoader.getMediaImagesWithAlbumNameCursor(albumId).toData { cursor ->
		val imageCount = cursor.count

		val bucketDisplayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
		val bucketDisplayName = cursor.getString(bucketDisplayNameColumn)

		val thumbnailImage = getMediaImage(cursor)

		MediaImageAlbum(
			albumId = albumId,
			albumName = bucketDisplayName,
			thumbnailImage = thumbnailImage,
			imageCount = imageCount
		)
	}

	private fun getAlbumIdList(): List<Int> = localMediaImageCursorLoader.getMediaBucketIdsCursor().toDataList { cursor ->
		val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
		cursor.getInt(bucketIdColumn)
	}

	private fun getMediaImage(cursor: Cursor): MediaImage {
		val imageIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
		val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
		val dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)
		val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)
		val orientationColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.ORIENTATION)

		val imageId = cursor.getLong(imageIdColumn)
		val albumId = cursor.getInt(bucketIdColumn)
		val dateModified = cursor.getLong(dateModifiedColumn)
		val mimeType = cursor.getString(mimeTypeColumn) ?: ""
		val orientation = cursor.getInt(orientationColumn)
		val contentUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + imageId)

		return MediaImage(
			id = imageId,
			albumId = albumId,
			contentUri = contentUri,
			mimeType = mimeType,
			dateModified = dateModified,
			orientation = orientation
		)
	}
}