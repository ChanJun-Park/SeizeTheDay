package com.jingom.seizetheday.domain

sealed class Feeling(val name: String) {
	object Happy: Feeling("Happy")
	object Joy: Feeling("Joy")
	object Hope: Feeling("Hope")
	object Thanks: Feeling("Thanks")
	object Pride: Feeling("Pride")
	object Serenity: Feeling("Serenity")
	object Awe: Feeling("Awe")
}
