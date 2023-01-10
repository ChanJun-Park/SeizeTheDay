package com.jingom.seizetheday.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ThanksRecordDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(thanksRecord: ThanksRecord)

	@Query("""
		SELECT *
		FROM thanks_record
	""")
	suspend fun getThanksRecordsFlow(): Flow<List<ThanksRecord>>
}