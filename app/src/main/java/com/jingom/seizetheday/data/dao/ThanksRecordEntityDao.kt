package com.jingom.seizetheday.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jingom.seizetheday.data.model.ThanksRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ThanksRecordEntityDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(thanksRecord: ThanksRecordEntity)

	@Query("""
		SELECT *
		FROM thanks_record_entity
		ORDER BY date DESC, id DESC
	""")
	fun getThanksRecordEntitiesFlow(): Flow<List<ThanksRecordEntity>>

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
}