package com.jingom.seizetheday.data.converter

import androidx.room.TypeConverter
import com.jingom.seizetheday.domain.Feeling

class FeelingTypeConverters {
	@TypeConverter
	fun toId(feeling: Feeling): Int = feeling.id

	@TypeConverter
	fun toFeeling(id: Int): Feeling = Feeling.getFeeling(id)

}