package com.jingom.seizetheday.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.jingom.seizetheday.data.db.model.ThanksRecordEntity
import com.jingom.seizetheday.data.db.model.ThanksRecordWithAttachedImagesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ThanksRecordEntityDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(thanksRecord: ThanksRecordEntity): Long

	@Query("""
		SELECT *
		FROM thanks_record_entity
		WHERE id = :id
	""")
	suspend fun getThanksRecord(id: Long): ThanksRecordEntity

	@Query("""
		SELECT *
		FROM thanks_record_entity
		ORDER BY date DESC, id DESC
	""")
	fun getThanksRecordEntitiesFlow(): Flow<List<ThanksRecordEntity>>

	@Transaction
	@Query("""
		SELECT *
		FROM thanks_record_entity
		ORDER BY date DESC, id DESC
		LIMIT :perPageSize OFFSET :offset
	""")
	suspend fun getThanksRecordWithAttachedImageEntities(offset: Int, perPageSize: Int): List<ThanksRecordWithAttachedImagesEntity>

	@Query(
		"""
		SELECT *
		FROM thanks_record_entity
		ORDER BY date DESC, id DESC
		LIMIT :perPageSize OFFSET :offset
	"""
	)
	suspend fun getThanksRecordEntities(offset: Int, perPageSize: Int): List<ThanksRecordEntity>

	@Query(
		"""
		SELECT id
		FROM thanks_record_entity
		ORDER BY date DESC, id DESC
		"""
	)
	suspend fun getThanksRecordEntityIds(): List<Int>

	@Query(
		"""
		SELECT COUNT(id)
		FROM thanks_record_entity
		WHERE date >= :thanksRecordDate AND id > :thanksRecordId
		"""
	)
	suspend fun getThanksRecordRowIndex(thanksRecordId: Long, thanksRecordDate: String): Int

	@Query(
		"""
		SELECT COUNT(*)
		FROM thanks_record_entity
		"""
	)
	fun selectThanksRecordCount(): Int

	@Query("""
		SELECT id
		FROM thanks_record_entity
		ORDER BY date DESC, id DESC
	""")
	fun selectThanksIdsInDateOrder(): List<Long>
}