package com.jingom.seizetheday.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.jingom.seizetheday.data.db.model.AttachedImageEntity

@Dao
interface AttachedImageEntityDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(vararg attachedImageEntity: AttachedImageEntity)
}