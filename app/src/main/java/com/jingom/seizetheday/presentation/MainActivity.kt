package com.jingom.seizetheday.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jingom.seizetheday.presentation.ui.theme.SeizeTheDayTheme
import com.jingom.seizetheday.presentation.write.WritingThanksScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			SeizeTheDayTheme {
				WritingThanksScreen()
			}
		}
	}
}