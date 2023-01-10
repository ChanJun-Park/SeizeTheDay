package com.jingom.seizetheday.domain

sealed class Feeling(
	val id: Int,
	val name: String
) {
	object Happy: Feeling(0,"Happy")
	object Joy: Feeling(1,"Joy")
	object Hope: Feeling(2,"Hope")
	object Thanks: Feeling(3,"Thanks")
	object Pride: Feeling(4,"Pride")
	object Serenity: Feeling(5,"Serenity")
	object Awe: Feeling(6,"Awe")

	companion object {
		fun getFeeling(id: Int): Feeling {
			return when (id) {
				Happy.id -> Happy
				Joy.id -> Joy
				Hope.id -> Hope
				Thanks.id -> Thanks
				Pride.id -> Pride
				Serenity.id -> Serenity
				Awe.id -> Awe
				else -> throw IllegalArgumentException()
			}
		}
	}
}
