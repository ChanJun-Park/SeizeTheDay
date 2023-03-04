package com.jingom.seizetheday.data.media

import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.WorkerThread

private val imageCollection =
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
		MediaStore.Images.Media.getContentUri(
			MediaStore.VOLUME_EXTERNAL
		)
	} else {
		MediaStore.Images.Media.EXTERNAL_CONTENT_URI
	}

private val bucketProjection = arrayOf(
	MediaStore.Images.ImageColumns.BUCKET_ID
)

private val imageProjection = arrayOf(
	MediaStore.Images.Media._ID,
	MediaStore.Images.Media.BUCKET_ID,
	MediaStore.Images.Media.DATE_MODIFIED,
	MediaStore.Images.Media.MIME_TYPE,
	MediaStore.Images.Media.ORIENTATION
)

private val imageWithAlbumNameProjection =
	imageProjection + MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME

private const val ORDER = MediaStore.Files.FileColumns.DATE_ADDED + " DESC"

class LocalMediaImageCursorLoader(private val context: Context) {

	@WorkerThread
	fun getMediaBucketIdsCursor(): Cursor? = context.contentResolver.query(
		/* uri = */ imageCollection,
		/* projection = */ bucketProjection,
		/* selection = */ null,
		/* selectionArgs = */ null,
		/* sortOrder = */ null
	)

	@WorkerThread
	fun getMediaImagesWithAlbumNameCursor(bucketId: Int = ALL_IMAGES_BUCKET_ID): Cursor? = context.contentResolver.query(
		/* uri = */ imageCollection,
		/* projection = */ imageWithAlbumNameProjection,
		/* selection = */ getBucketIdSelection(bucketId),
		/* selectionArgs = */ null,
		/* sortOrder = */ null
	)

	@WorkerThread
	fun getMediaImagesCursor(bucketId: Int = ALL_IMAGES_BUCKET_ID): Cursor? = context.contentResolver.query(
		/* uri = */ imageCollection,
		/* projection = */ imageProjection,
		/* selection = */ getBucketIdSelection(bucketId),
		/* selectionArgs = */ null,
		/* sortOrder = */ ORDER
	)

	private fun getBucketIdSelection(bucketId: Int): String {
		return if (bucketId == ALL_IMAGES_BUCKET_ID) {
			""
		} else {
			"${MediaStore.Images.ImageColumns.BUCKET_ID} = $bucketId"
		}
	}

	companion object {
		const val ALL_IMAGES_BUCKET_ID = -1
	}
}