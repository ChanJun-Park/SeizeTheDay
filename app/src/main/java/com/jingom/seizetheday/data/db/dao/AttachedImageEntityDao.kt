package com.jingom.seizetheday.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jingom.seizetheday.data.db.model.AttachedImageEntity
import com.jingom.seizetheday.domain.model.AttachedImage

@Dao
interface AttachedImageEntityDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(vararg attachedImageEntity: AttachedImageEntity)

	@Query(
		"""
		SELECT *
		FROM attached_image_entity
		WHERE thanks_record_id = :thanksRecordId
	"""
	)
	suspend fun selectAllImageWithThanksRecordId(thanksRecordId: Long): List<AttachedImageEntity>
}