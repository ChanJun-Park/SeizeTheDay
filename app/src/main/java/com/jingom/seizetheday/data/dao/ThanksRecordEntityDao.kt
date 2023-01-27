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
		ORDER BY date DESC
	""")
	fun getThanksRecordEntitiesFlow(): Flow<List<ThanksRecordEntity>>
}