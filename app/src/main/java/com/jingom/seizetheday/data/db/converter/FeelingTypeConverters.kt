package com.jingom.seizetheday.data.db.converter

import androidx.room.TypeConverter
import com.jingom.seizetheday.domain.model.Feeling

class FeelingTypeConverters {
	@TypeConverter
	fun toId(feeling: Feeling): Int = feeling.id

	@TypeConverter
	fun toFeeling(id: Int): Feeling = Feeling.getFeeling(id)

}